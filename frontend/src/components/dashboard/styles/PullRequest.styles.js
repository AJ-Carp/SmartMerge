import styled from "styled-components";
import { fadeInUp } from "./Dashboard.styles";

// everything below adds style to a single repo's pull request page

export const PRWrapper = styled.div`
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f6f9 0%, #eef2f7 100%);
  background-size: 100% 100%;
  animation: ${fadeInUp} 0.6s ease;
  display: flex;
  flex-direction: column;
`;

export const PRHeader = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2.2rem 2.2rem 1.2rem 2.2rem;
  @media (max-width: 600px) {
    flex-direction: column;
    gap: 0.7rem;
    padding: 1.2rem 0.7rem 0.7rem 0.7rem;
    align-items: stretch;
  }
`;

export const PRBackButton = styled.button`
  background: rgba(11, 108, 255, 0.06);
  border: none;
  border-radius: 50%;
  padding: 0.5rem;
  cursor: pointer;
  transition: background 0.18s, transform 0.18s;
  display: flex;
  align-items: center;
  &:hover {
    background: rgba(11, 108, 255, 0.12);
    transform: scale(1.08);
  }
  svg path {
    stroke: #0b6cff;
  }
`;

export const PRHeading = styled.h2`
  color: #0b2b4a;
  font-size: 1.4rem;
  font-weight: 700;
  margin: 0;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  flex: 1;
  text-align: center;
`;

export const PRRepoTitle = styled.div`
  color: #0b2b4a;
  font-size: 1.02rem;
  font-weight: 600;
  background: rgba(11, 108, 255, 0.06);
  border-radius: 10px;
  padding: 0.45rem 1rem;
  margin-left: 1rem;
  @media (max-width: 600px) {
    margin-left: 0;
    text-align: right;
    align-self: flex-end;
  }
`;

export const PRBody = styled.div`
  flex: 1;
  width: 100%;
  padding: 0 2.2rem 2.2rem 2.2rem;
  @media (max-width: 600px) {
    padding: 0 0.7rem 1.2rem 0.7rem;
  }
`;

export const PRList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

export const PRListItem = styled.li`
  background: #ffffff;
  border-radius: 12px;
  padding: 1.1rem 1.2rem;
  margin-bottom: 1.1rem;
  color: #0b2b4a;
  font-size: 1.08rem;
  box-shadow: 0 12px 30px rgba(11, 44, 77, 0.06);
  transition: box-shadow 0.18s, transform 0.18s;
  display: flex;
  align-items: center;
  gap: 1rem;
  &:hover {
    box-shadow: 0 18px 38px rgba(11, 44, 77, 0.08);
    transform: translateY(-4px) scale(1.01);
  }
`;

export const PRNoData = styled.div`
  color: #6b7a8a;
  margin-top: 2rem;
  font-style: italic;
  text-align: center;
`;

export const PRAuthor = styled.div`
  color: #6b7a8a;
  font-size: 0.98rem;
  margin-bottom: 0.2rem;
  span {
    color: #0b2b4a;
    font-weight: 500;
    margin-left: 0.3rem;
  }
`;

// the status word itself gets colored by what it says — open/closed/reviewed each get their own
export const PRStatus = styled.div`
  color: #6b7a8a;
  font-size: 0.98rem;
  margin-bottom: 0.2rem;
  span {
    color: ${({ status }) =>
      status === "OPEN"
        ? "#06b6d4"
        : status === "CLOSED"
        ? "#ff6a6a"
        : status === "REVIEWED"
        ? "#f59e0b"
        : "#6b7a8a"};
    font-weight: 600;
    margin-left: 0.3rem;
    text-transform: capitalize;
  }
`;

export const PRDates = styled.div`
  color: #6b7a8a;
  font-size: 0.93rem;
  display: flex;
  gap: 1.2rem;
  margin-bottom: 0.2rem;
  span {
    color: #6b7a8a;
    font-weight: 400;
  }
`;

export const PRGoToButton = styled.button`
  background: rgba(11, 108, 255, 0.08);
  border: none;
  border-radius: 50%;
  padding: 0.5rem;
  margin-left: 1.2rem;
  cursor: pointer;
  transition: background 0.18s, transform 0.18s;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 2.2rem;
  width: 2.2rem;
  &:hover {
    background: rgba(11, 108, 255, 0.14);
    transform: scale(1.08);
  }
  svg {
    display: block;
  }
  svg path {
    stroke: #0b6cff;
  }
`;

export const PRFilters = styled.div`
  display: flex;
  gap: 1.2rem;
  align-items: center;
  padding: 0 2.2rem 1.2rem 2.2rem;
  @media (max-width: 600px) {
    flex-direction: column;
    gap: 0.7rem;
    padding: 0 0.7rem 1rem 0.7rem;
    align-items: stretch;
  }
`;

export const PRSelect = styled.select`
  background: #fafbfc;
  color: #0b2b4a;
  border: 1px solid rgba(11, 44, 77, 0.08);
  border-radius: 10px;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  outline: none;
  transition: background 0.18s, color 0.18s, border 0.18s, box-shadow 0.18s;
  box-shadow: 0 6px 18px rgba(11, 44, 77, 0.04);

  &:focus {
    background: #fff;
    border: 1.5px solid #0b6cff;
    color: #0b6cff;
    box-shadow: 0 0 0 4px rgba(11, 108, 255, 0.08);
  }

  option {
    background: #fff;
    color: #0b2b4a;
  }
`;
