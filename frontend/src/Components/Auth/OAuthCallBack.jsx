import React, { useEffect } from 'react';
import { useAuth } from '../hooks/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../api/api';

import {
  AuthWrapper,
  LoginFormContainer,
  LoadingText,
  Spinner
} from './styles/Auth.styles';

function OAuthCallBack() {
  const { setUser } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    api.get("/profile")
      // on success store the user in local storage and navigate to dashboard
      .then(res => {
        setUser(res.data);
        setTimeout(() => {
          navigate("/dashboard");
        }, 1000)
      })
      // on error navigate back to login
      .catch(err => {
        setUser(null);
        navigate("/");
        console.log(err)
      });
  }, [setUser, navigate]);

  return (
    <AuthWrapper>
      <LoginFormContainer>
        <LoadingText>Logging you in...</LoadingText>
        <Spinner />
      </LoginFormContainer>
    </AuthWrapper>
  );
}

export default OAuthCallBack
