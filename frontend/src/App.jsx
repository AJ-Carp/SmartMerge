import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { useAuth } from './Components/hooks/AuthContext'
import Auth from './Components/Auth/Auth'
import OAuthCallBack from './Components/Auth/OAuthCallBack'
import AuthenticationSuccess from './Components/Auth/AuthenticationSuccess'
import DashBoard from './Components/dashboard/DashBoard'

function App() {
  const { isLoggedIn } = useAuth()

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={isLoggedIn ? <DashBoard /> : <Auth />} />
        <Route path="/call-back" element={<AuthenticationSuccess />} />
        <Route path="/authenticate" element={<OAuthCallBack />} />
        <Route path="/dashboard" element={isLoggedIn ? <DashBoard /> : <Auth />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
