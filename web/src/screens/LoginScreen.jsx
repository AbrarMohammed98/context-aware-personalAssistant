export default function LoginScreen() {
  const handleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  return (
    <div className="login-container">
      <h1 className="login-title">Context-Aware Assistant</h1>
      <p className="login-subtitle">Your intelligent task and reminder companion</p>
      <button className="google-btn" onClick={handleLogin}>
        Login with Google
      </button>
    </div>
  );
}