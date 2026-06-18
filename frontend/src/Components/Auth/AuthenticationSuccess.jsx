import React, { useEffect } from 'react';
import styled, { keyframes } from 'styled-components';
import { useNavigate } from "react-router-dom";

const Wrapper = styled.div`
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #f5f6f9 0%, #eef1f7 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const pop = keyframes`
  0% { transform: scale(0.7); opacity: 0; }
  60% { transform: scale(1.1); opacity: 1; }
  80% { transform: scale(0.95); }
  100% { transform: scale(1); }
`;

const SuccessCircle = styled.div`
  width: 110px;
  height: 110px;
  border-radius: 50%;
  background: #d4e6ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
  animation: ${pop} 0.6s cubic-bezier(0.23, 1, 0.32, 1);
  box-shadow: 0 12px 32px rgba(11,108,255,0.15);
`;

const CheckMark = styled.svg`
  width: 54px;
  height: 54px;
  stroke: #0b6cff;
  stroke-width: 6;
  stroke-linecap: round;
  stroke-linejoin: round;
  fill: none;
  animation: ${pop} 0.6s 0.05s cubic-bezier(0.23, 1, 0.32, 1);
`;

const SuccessText = styled.h2`
  color: #0b2b4a;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 1.6rem;
  margin-top: 0.25rem;
  text-align: center;
  letter-spacing: 0.2px;
  animation: ${pop} 0.6s 0.12s cubic-bezier(0.23, 1, 0.32, 1);
`;

/**
   * the user will be redirected this component 
   * after a successfult authentication in the 
   * backend.
   */
function AuthenticationSuccess() {
  const navigate = useNavigate();

  // setting timeout to let
  // the animation finish
  useEffect(() => {
    setTimeout(() => {
        navigate("/authenticate")
      }, 1500)
  }, [navigate])

  return (
    <Wrapper>

      <SuccessCircle>
        <CheckMark viewBox="0 0 52 52">
          <path d="M14 27l8 8 16-16" />
        </CheckMark>
      </SuccessCircle>

      <SuccessText>Authentication Successful!</SuccessText>

    </Wrapper>
  );
}

export default AuthenticationSuccess;
