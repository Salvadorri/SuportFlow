import Index from "../components/login/index";
import { AuthProvider } from "../contexts/AuthContext"; // Importe AuthProvider

function Login() {
  return (
    <AuthProvider>
      <Index />
    </AuthProvider>
  );
}

export default Login;
