import styled from "styled-components";
import { fadeInUp } from "./Dashboard.styles"; // reuse the same fade so the animations line up

// styling for the repo list

export const RepositoriesTabWrapper = styled.div`
  width: 100%;
  animation: ${fadeInUp} 0.7s ease;
`;

export const RepositoriesTabHeading = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(11, 108, 255, 0.05);
  border-radius: 12px;
  padding: 1rem;
  margin-bottom: 1.6rem;
  box-shadow: 0 6px 18px rgba(11, 44, 77, 0.04);
`;

export const RepositoriesTabTitle = styled.h2`
  font-size: 1.4rem;
  font-weight: 700;
  color: #0b2b4a;
  margin: 0;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
`;

// the add / remove repos button up top
export const AddRepoButton = styled.button`
  background: ${({ className }) =>
    className && className.includes('active')
      ? 'rgba(11, 108, 255, 0.13)'
      : 'rgba(11, 108, 255, 0.10)'};
  color: #0b2b4a;
  border: none;
  border-radius: 999px; // pill shape
  padding: 0.65rem 1.4rem;
  font-size: 1rem;
  font-weight: 500;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  cursor: pointer;
  box-shadow: 0 6px 18px rgba(11, 44, 77, 0.04);
  transition: background 0.18s, color 0.18s, transform 0.18s;
  display: flex;
  align-items: center;
  gap: 0.6rem;

  &:hover {
    background: rgba(11, 108, 255, 0.14);
    color: #0b2b4a;
    transform: translateY(-1px) scale(1.02);
    box-shadow: 0 8px 22px rgba(11, 44, 77, 0.06);
  }

  &:active {
    background: rgba(139, 69, 255, 0.20);
    color: #fff;
    transform: none;
  }
`;

export const PullRequestTabWrapper = styled.div`
    width: 100%;
`;

export const RepositoriesDivider = styled.div`
  width: 100%;
  margin: 1.2rem 0 1.5rem 0;
  padding-bottom: 0.5rem;
  border-bottom: 1.5px solid rgba(11, 108, 255, 0.08);
  color: #6b7a8a;
  font-weight: 600;
  font-size: 1.02rem;
  letter-spacing: 0.02em;
  display: flex;
  align-items: center;
`;

export const RepositoriesTable = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.1rem;
  width: 100%;
  animation: ${fadeInUp} 0.5s;
`;

export const RepositoryRow = styled.div`
  background: #ffffff;
  border-radius: 12px;
  padding: 1.1rem 1.2rem;
  display: flex;
  align-items: center;
  gap: 1.2rem;
  box-shadow: 0 12px 30px rgba(11, 44, 77, 0.06);
  transition: box-shadow 0.18s, transform 0.18s;
  &:hover {
    box-shadow: 0 18px 38px rgba(11, 44, 77, 0.08);
    transform: translateY(-4px) scale(1.01);
  }
`;

export const RepositoryName = styled.div`
  display: flex;
  align-items: center;
  gap: 0.7rem;
  font-weight: 600;
  color: #0b2b4a;
  font-size: 1.08rem;
  flex: 2;
  svg {
    flex-shrink: 0;
  }
`;

export const RepositoryMeta = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #6b7a8a;
  font-size: 0.98rem;
  flex: 1;
`;

export const RepositoryActions = styled.div`
  display: flex;
  align-items: center;
  gap: 0.7rem;
  a {
    color: #0b6cff;
    background: rgba(11, 108, 255, 0.08);
    border-radius: 50%;
    padding: 0.35rem;
    display: flex;
    align-items: center;
    transition: background 0.18s;
    &:hover {
      background: rgba(11, 108, 255, 0.14);
    }
    svg {
      display: block;
    }
  }
`;

export const RemoveRepoButton = styled.button`
  background: rgba(255, 106, 106, 0.06);
  border: none;
  border-radius: 50%;
  padding: 0.4rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.18s, transform 0.18s;
  margin-left: 0.2rem;

  &:hover {
    background: rgba(255, 106, 106, 0.14);
    transform: scale(1.08);
  }
  &:active {
    background: rgba(255, 106, 106, 0.2);
  }
  svg {
    display: block;
  }
`;

export const GoToRepoButton = styled.button`
  background: rgba(11, 108, 255, 0.08);
  border: none;
  border-radius: 50%;
  padding: 0.4rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.18s, transform 0.18s;
  margin-right: 0.2rem;

  &:hover {
    background: rgba(11, 108, 255, 0.14);
    transform: scale(1.08);
  }
  &:active {
    background: rgba(11, 108, 255, 0.2);
  }
  svg {
    display: block;
  }
`;
