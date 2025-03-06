import { useNavigate } from "@tanstack/react-router";
import React, { useCallback, useEffect, useState } from "react"; // Import useEffect
import login, { getToken, isTokenExpired } from "./login-jwt"; // Import getToken
import { validateLoginForm, ValidationResult } from "./validation.ts";

function Index() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState<ValidationResult["errors"]>({});
  const [rememberMe, setRememberMe] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [loginError, setLoginError] = useState<string | null>(null);

  const navigate = useNavigate();

  // Check for existing token on component mount
  useEffect(() => {
    const token = getToken();
    if (token && !isTokenExpired()) {
      // Redirect to dashboard if a valid token exists
      navigate({ to: "/dashboard" });
    }
  }, [navigate]); // Add navigate to the dependency array

  const handleSubmit = useCallback(
    async (event: React.FormEvent) => {
      event.preventDefault();
      setLoginError(null);

      const validationResult = validateLoginForm(email, password);

      if (validationResult.isValid) {
        setIsLoading(true);
        try {
          const userData = await login(email, password);

          if (userData) {
            console.log("Login successful. User Data:", userData);

            if (userData.userPermissions.includes("ADMIN")) {
              navigate({ to: "/dashboard" });
            } else {
              navigate({ to: "/dashboard" });
            }
          } else {
            setLoginError("Login failed. No user data returned.");
          }
        } catch (error) {
          console.error("Login failed:", error);
          if (error instanceof Error) {
            setLoginError(error.message);
          } else {
            setLoginError("An unexpected error occurred.");
          }
        } finally {
          setIsLoading(false);
        }
      } else {
        setErrors(validationResult.errors);
        console.log("Formulário inválido");
      }
    },
    [email, password, navigate]
  );

  const handleRememberMeChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setRememberMe(event.target.checked);
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-900">
      <div className="w-full max-w-md p-8 space-y-8 bg-gray-800 rounded-xl shadow-2xl">
        <h2 className="text-3xl font-bold text-center text-white">
          SuportFlowAI
        </h2>

        <form className="space-y-6" onSubmit={handleSubmit}>
          <div>
            <label
              htmlFor="email"
              className="block mb-2 text-sm font-medium text-gray-300"
            >
              E-mail
            </label>
            <input
              type="email"
              id="email"
              className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
              placeholder="seu@email.com"
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
                if (errors.email) {
                  setErrors((prevErrors) => ({ ...prevErrors, email: "" }));
                }
              }}
              required
            />
            {errors.email && (
              <p className="text-red-500 text-sm mt-1">{errors.email}</p>
            )}
          </div>

          <div>
            <label
              htmlFor="password"
              className="block mb-2 text-sm font-medium text-gray-300"
            >
              Senha
            </label>
            <input
              type="password"
              id="password"
              className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
              placeholder="••••••••"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                if (errors.password) {
                  setErrors((prevErrors) => ({ ...prevErrors, password: "" }));
                }
              }}
              required
            />
            {errors.password && (
              <p className="text-red-500 text-sm mt-1">{errors.password}</p>
            )}
          </div>

          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <input
                id="remember-me"
                type="checkbox"
                className="w-4 h-4 text-blue-600 bg-gray-700 rounded border-gray-600 focus:ring-blue-500 focus:ring-2"
                checked={rememberMe}
                onChange={handleRememberMeChange}
              />
              <label
                htmlFor="remember-me"
                className="ml-2 text-sm font-medium text-gray-300"
              >
                Lembrar de mim
              </label>
            </div>

            <a
              href="#"
              className="text-sm font-medium text-blue-500 hover:underline"
            >
              Esqueceu a senha?
            </a>
          </div>
          {loginError && (
            <p className="text-red-500 text-sm mt-1">{loginError}</p>
          )}
          <button
            type="submit"
            className="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
            disabled={isLoading}
          >
            {isLoading ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Index;
