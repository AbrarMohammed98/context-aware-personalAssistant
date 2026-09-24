import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import './App.css';
import LoginScreen from './screens/LoginScreen';
import AuthCallback from './screens/AuthCallback';
import DashboardScreen from './screens/DashboardScreen';
import ProtectedRoute from './components/ProtectedRoute';
function App() {
  return (
    <>
      <Toaster position="top-center" toastOptions={{ style: { background: '#18181b', color: '#e4e4e7' } }} />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginScreen />} />
          <Route path="/auth/callback" element={<AuthCallback />} />
          <Route path="/dashboard" element={<ProtectedRoute><DashboardScreen /></ProtectedRoute>}/>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;