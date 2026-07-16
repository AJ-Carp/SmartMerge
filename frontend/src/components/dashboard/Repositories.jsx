import React, { useEffect, useState } from 'react';
import {
  RepositoriesTabHeading,
  RepositoriesTabTitle,
  RepositoriesTabWrapper,
  AddRepoButton,
  RepositoriesDivider,
  RepositoriesTable,
  RepositoryRow,
} from './styles/DashboardBody.styles';
import { useAuth } from '../hooks/AuthContext';
import api from '../api/api';
import RepoListItem from './RepoListItem';

// our github app slug — whatever comes after /apps/ in its url
const APP_SLUG = "smart-merge";

function Repositories() {
  const { user } = useAuth();
  const [repositories, setRepositories] = useState([]);
  const handleAddRepository = () => {
    // adding + removing repos is all handled on github's install page, not from here.
    // the backend just reacts to the install webhooks and keeps its repo list in sync.
    const url = `https://github.com/apps/${APP_SLUG}/installations/new?target_id=${user.userId}`;
    window.open(url, "_blank")
  };

  // leaving this off for now — we don't have a backend route to delete a repo yet
  // (would be something like DELETE /repository/{repoId}/installation/{installationId}).
  // for now removals happen on github and come back to us via the removed webhook.
  // whenever we add that route: uncomment, pass onRemove into RepoListItem, drop a button in.
  // filtering on repoId here because that's the field the Repo model sends back, not id.
  /*
  const handleRemoveRepository = async (repoId, installationId) => {
    try {
      await api.delete(`/repository/${repoId}/installation/${installationId}`);
      setRepositories(repositories.filter(r => r.repoId !== repoId));
    } catch (err) {
      console.log(err);
    }
  };
  */

  // pull down this user's repos when the tab mounts
  useEffect(() => {
    api.get(`/repository/user/${user.userId}`)
      .then(res => {
        setRepositories(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }, [user.userId]);

  // flip the button text once they actually have some repos
  const addRepoLabel = repositories.length === 0
    ? "Add Repo"
    : (
      <>
        Add <span style={{ color: "#bbaaff", fontWeight: 600 }}>/</span> Remove Repos
      </>
    );

  return (
    <RepositoriesTabWrapper>
      <RepositoriesTabHeading>
        <RepositoriesTabTitle>Repositories</RepositoriesTabTitle>
        <AddRepoButton onClick={handleAddRepository}>
          <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
            <path d="M12 5v14m7-7H5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
          {addRepoLabel}
        </AddRepoButton>
      </RepositoriesTabHeading>

      <RepositoriesDivider>
        <span>Your Repositories</span>
      </RepositoriesDivider>

      <RepositoriesTable>
        {repositories.length === 0 && (
          <RepositoryRow style={{ justifyContent: 'center', color: '#aaa', fontStyle: 'italic' }}>
            No repositories found.
          </RepositoryRow>
        )}
        {repositories.map(repo => (
          <RepoListItem key={repo.repoId} repo={repo} />
        ))}
      </RepositoriesTable>
    </RepositoriesTabWrapper>
  );
}

export default Repositories;
