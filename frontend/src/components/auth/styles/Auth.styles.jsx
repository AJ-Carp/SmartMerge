import styled, { keyframes } from 'styled-components';

export const AuthWrapper = styled.div`
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #f5f6f9 0%, #eef1f7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const LoginFormContainer = styled.div`
  background: #ffffff;
  padding: 3rem 2.5rem;
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(11, 43, 74, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.25rem;
  min-width: 320px;
`;

export const WelcomeHeading = styled.h1`
  margin: 0;
  color: #0b2b4a;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 1.5rem;
  text-align: center;
`;

export const GithubButton = styled.button`
  display: flex;
  align-items: center;
  gap: 0.6rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  background: #0b2b4a;
  color: #ffffff;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s ease;

  &:hover {
    background: #0b6cff;
  }
`;

export const GithubIcon = () => (
  <svg width="20" height="20" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
    <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.02-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z" />
  </svg>
);

export const LoadingText = styled.p`
  margin: 0 0 1.5rem;
  color: #0b2b4a;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 1.1rem;
`;

const spin = keyframes`
  to { transform: rotate(360deg); }
`;

export const Spinner = styled.div`
  width: 42px;
  height: 42px;
  border: 4px solid #d4e6ff;
  border-top-color: #0b6cff;
  border-radius: 50%;
  animation: ${spin} 0.8s linear infinite;
`;
