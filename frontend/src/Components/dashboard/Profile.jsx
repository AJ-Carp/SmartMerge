import { useAuth } from '../hooks/AuthContext';
import { useNavigate } from 'react-router-dom';

import {
    ProfileAvatar,
    ProfileName,
    LogoutButton
} from './styles/Dashboard.styles';

function Profile() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate('/');
    };

    return (
        <>
            <ProfileAvatar src={user.avatarUrl} alt="avatar" />
            <ProfileName>{user.login}</ProfileName>
            <LogoutButton title="Logout" onClick={handleLogout}>
                <svg width="22" height="22" fill="none" viewBox="0 0 24 24">
                    <path d="M16 17l5-5m0 0l-5-5m5 5H9m4 5v1a2 2 0 01-2 2H5a2 2 0 01-2-2V6a2 2 0 012-2h6a2 2 0 012 2v1" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
            </LogoutButton>
        </>
    );
}

export default Profile
