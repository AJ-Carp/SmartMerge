import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import {
    PRWrapper,
    PRHeader,
    PRBackButton,
    PRHeading,
    PRRepoTitle,
    PRBody,
    PRList,
    PRListItem,
    PRNoData,
    PRAuthor,
    PRStatus,
    PRDates,
    PRGoToButton,
    PRFilters,
    PRSelect
} from './styles/PullRequest.styles';
import api from '../api/api';

function formatDate(date) {
    if (!date) return '-';
    return new Date(date).toLocaleString();
}

const statusOptions = [
    { value: '', label: 'All Statuses' },
    { value: 'OPEN', label: 'Open' },
    { value: 'CLOSED', label: 'Closed' },
    { value: 'REVIEWED', label: 'Reviewed' },
    { value: 'MERGED', label: 'Merged' }
];

const sortOptions = [
    { value: 'openedAt', label: 'Opened Date' },
    { value: 'reviewedAt', label: 'Reviewed Date' },
    { value: 'closedAt', label: 'Closed Date' }
];

function RepositoryPullRequests(props) {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const [pullRequests, setPullRequests] = useState([]);
    const [statusFilter, setStatusFilter] = useState('');
    const [sortBy, setSortBy] = useState('openedAt');
    const [sortOrder, setSortOrder] = useState('asc'); // oldest first by default

    // the PR payload doesn't carry the repo name, so we only get it if whoever sent us
    // here passed it along. otherwise fall back to something generic.
    const repoName = props.repoName || location.state?.repoName || "Repository";

    // grab the PRs for whatever repo id is in the url (refetches if that id changes)
    useEffect(() => {
        api.get(`/pull-requests/repository/${id}`)
            .then(res => {
                setPullRequests(res.data)
            })
            .catch(err => console.log(err));
    }, [id]);

    // drop anything that doesn't match the status, then order by whichever date they picked.
    // missing dates fall back to epoch so they sink to the bottom instead of blowing up.
    const filteredPRs = pullRequests
        .filter(pr => !statusFilter || pr.status === statusFilter)
        .sort((a, b) => {
            const aDate = a[sortBy] ? new Date(a[sortBy]) : new Date(0);
            const bDate = b[sortBy] ? new Date(b[sortBy]) : new Date(0);
            if (sortOrder === 'asc') return aDate - bDate;
            return bDate - aDate;
        });

    return (
        <PRWrapper>
            <PRHeader>
                <PRBackButton onClick={() => navigate(-1)} title="Back to repositories">
                    <svg width="22" height="22" fill="none" viewBox="0 0 24 24">
                        <path d="M15 19l-7-7 7-7" stroke="#8b45ff" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                    </svg>
                </PRBackButton>
                <PRHeading>Pull Requests</PRHeading>
                <PRRepoTitle>{repoName}</PRRepoTitle>
            </PRHeader>
            <PRFilters>
                <PRSelect
                    value={statusFilter}
                    onChange={e => setStatusFilter(e.target.value)}
                >
                    {statusOptions.map(opt => (
                        <option key={opt.value} value={opt.value}>{opt.label}</option>
                    ))}
                </PRSelect>
                <PRSelect
                    value={sortBy}
                    onChange={e => setSortBy(e.target.value)}
                >
                    {sortOptions.map(opt => (
                        <option key={opt.value} value={opt.value}>{`Sort by ${opt.label}`}</option>
                    ))}
                </PRSelect>
                <PRSelect
                    value={sortOrder}
                    onChange={e => setSortOrder(e.target.value)}
                >
                    <option value="asc">Oldest First</option>
                    <option value="desc">Newest First</option>
                </PRSelect>
            </PRFilters>
            <PRBody>
                {filteredPRs.length === 0 ? (
                    <PRNoData>No pull requests found.</PRNoData>
                ) : (
                    <PRList>
                        {filteredPRs.map(pr => (
                            <PRListItem key={pr.id}>
                                <div style={{ flex: 1 }}>
                                    <div style={{ fontWeight: 600, fontSize: "1.08rem", marginBottom: 4 }}>
                                        {pr.title}
                                    </div>
                                    <PRAuthor>
                                        Author: <span>{pr.authorName}</span>
                                    </PRAuthor>
                                    <PRStatus status={pr.status}>
                                        Status: <span>{pr.status}</span>
                                    </PRStatus>
                                    <PRDates>
                                        <span>Opened: {formatDate(pr.openedAt)}</span>
                                        <span>Reviewed: {formatDate(pr.reviewedAt)}</span>
                                        <span>Closed: {formatDate(pr.closedAt)}</span>
                                    </PRDates>
                                </div>
                                <PRGoToButton
                                    as="a"
                                    href={pr.url}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    title="View PR on GitHub"
                                >
                                    <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                                        <path d="M9 18l6-6-6-6" stroke="#8b45ff" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                    </svg>
                                </PRGoToButton>
                            </PRListItem>
                        ))}
                    </PRList>
                )}
            </PRBody>
        </PRWrapper>
    );
}

export default RepositoryPullRequests;
