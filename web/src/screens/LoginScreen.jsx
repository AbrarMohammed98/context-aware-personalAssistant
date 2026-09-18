export default function LoginScreen() {
    const handleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };
    return(
        <div>
            <h1>Context-Aware Assistant</h1>
            <button> onClick={handleLogin}>Login with Google</button>
        </div>

    );
}