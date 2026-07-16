import styled, { keyframes } from "styled-components";

export const fadeInUp = keyframes`
  from { opacity: 0; transform: translateY(30px);}
  to { opacity: 1; transform: translateY(0);}
`;

export const DashboardWrapper = styled.div`
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f6f9 0%, #eef2f7 100%);
  display: flex;
`;

export const Sidebar = styled.aside`
  width: 260px;
  background: #ffffff;
  backdrop-filter: blur(6px);
  border-right: 1px solid rgba(11, 44, 77, 0.06);
  box-shadow: 0 8px 28px rgba(11, 44, 77, 0.04);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2.5rem 1.2rem 1.2rem 1.2rem;
  position: relative;
  z-index: 2;

  @media (max-width: 900px) {
    width: 70px;
    padding: 1.2rem 0.5rem;
  }
`;

export const SidebarTab = styled.button`
  width: 100%;
  background: ${({ className }) =>
    className && className.includes('active')
      ? 'rgba(11, 108, 255, 0.08)'
      : 'transparent'};
  color: #0b2b4a;
  border: none;
  border-radius: 12px;
  padding: 0.95rem 1.1rem;
  margin-bottom: 1rem;
  font-size: 1.05rem;
  font-weight: 500;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  display: flex;
  align-items: center;
  gap: 0.7rem;
  cursor: pointer;
  box-shadow: none;
  transition: background 0.18s, color 0.18s, transform 0.18s;

  &:hover {
    background: rgba(11, 108, 255, 0.06);
    color: #0b2b4a;
    transform: translateY(-1px) scale(1.01);
  }

  &:active {
    background: rgba(11, 108, 255, 0.12);
    color: #0b2b4a;
    transform: none;
  }
`;

export const ProfileSection = styled.div`
  margin-top: auto;
  width: 100%;
  background: rgba(11, 108, 255, 0.04);
  border-radius: 12px;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.9rem;
  box-shadow: 0 6px 18px rgba(11, 44, 77, 0.04);
  animation: ${fadeInUp} 0.6s ease;

  @media (max-width: 900px) {
    gap: 0.7rem;
    padding: 0.7rem 0.3rem;
  }
`;

export const ProfileAvatar = styled.img`
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #0b6cff;
  background: #fff;
  box-shadow: 0 2px 6px rgba(11, 44, 77, 0.06);
  margin-bottom: 0.3rem;
`;

export const ProfileName = styled.div`
  color: #0b2b4a;
  font-weight: 700;
  font-size: 1.05rem;
  font-family: "Inter", -apple-system, BlinkMacSystemFont, sans-serif;
  margin-bottom: 0.2rem;
`;

export const LogoutButton = styled.button`
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: ${({ className }) =>
    className && className.includes('active')
      ? 'rgba(11, 108, 255, 0.08)'
      : 'transparent'};
  color: #0b2b4a;
  border: none;
  border-radius: 12px;
  font-size: 1.35rem;
  cursor: pointer;
  box-shadow: none;
  transition: background 0.18s, color 0.18s, transform 0.18s;
  margin-top: 0.2rem;

  &:hover {
    background: rgba(11, 108, 255, 0.06);
    color: #0b2b4a;
    transform: translateY(-1px) scale(1.07);
  }

  &:active {
    background: rgba(11, 108, 255, 0.12);
    color: #0b2b4a;
    transform: none;
  }
`;

export const MainContent = styled.main`
  flex: 1;
  padding: 3rem 2.5rem;
  animation: ${fadeInUp} 0.8s ease;
  min-width: 0;

  @media (max-width: 900px) {
    padding: 1.5rem 0.7rem;
  }
`;
