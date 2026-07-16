import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { useAuth } from './components/hooks/AuthContext'
import { MessageProvider } from './components/alerts/MessageContext'
import Auth from './components/auth/Auth'
import OAuthCallBack from './components/auth/OAuthCallBack'
import AuthenticationSuccess from './components/auth/AuthenticationSuccess'
import DashBoard from './components/dashboard/DashBoard'
import RepositoryPullRequests from './components/dashboard/RepositoryPullRequests'

function App() {
  const { isLoggedIn } = useAuth()

  return (
    <MessageProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={isLoggedIn ? <DashBoard /> : <Auth />} />
          <Route path="/call-back" element={<AuthenticationSuccess />} />
          <Route path="/authenticate" element={<OAuthCallBack />} />
          <Route path="/dashboard" element={isLoggedIn ? <DashBoard /> : <Auth />} />
          <Route path="/dashboard/repository/:id" element={isLoggedIn ? <RepositoryPullRequests /> : <Auth />} />
        </Routes>
      </BrowserRouter>
    </MessageProvider>
  )
}

export default App
