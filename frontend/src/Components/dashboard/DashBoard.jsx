import { useAuth } from '../hooks/AuthContext';

// Placeholder dashboard — just enough to confirm the login flow works.
function DashBoard() {
  const { user, logout } = useAuth();

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '1rem',
        fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, sans-serif",
        color: '#0b2b4a',
      }}
    >
      <h1>You're logged in 🎉</h1>
      <p>Welcome{user?.login ? `, ${user.login}` : ''}!</p>
      <button
        onClick={logout}
        style={{
          padding: '0.6rem 1.25rem',
          border: 'none',
          borderRadius: '8px',
          background: '#0b2b4a',
          color: '#fff',
          fontSize: '1rem',
          cursor: 'pointer',
        }}
      >
        Log out
      </button>
    </div>
  );
}

export default DashBoard
