import {
  AuthWrapper,
  LoginFormContainer,
  WelcomeHeading,
  GithubButton,
  GithubIcon
} from './styles/Auth.styles';

function Auth() {
  const handleLogin = () => {
    window.location.href = `${import.meta.env.VITE_API_URL}/oauth2/authorization/github`;
  };

  return (
    <AuthWrapper>
      <LoginFormContainer>

        <WelcomeHeading>Welcome to SmartMerge</WelcomeHeading>

        <GithubButton onClick={handleLogin}>
          <GithubIcon />
          Login with GitHub
        </GithubButton>

      </LoginFormContainer>
    </AuthWrapper>
  );
}

export default Auth
