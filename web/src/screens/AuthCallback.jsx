import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

export default function AuthCallback() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get('token');
    if (token) {
      localStorage.setItem('jwt_token', token);
      navigate('/dashboard');
    } else {
      navigate('/');
    }
  }, [searchParams, navigate]);

  return <p>Logging you in...</p>;
}