// src/contexts/AuthContext.tsx
import { useNavigate } from "@tanstack/react-router";
import React, {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import {
  AuthResult,
  clearClientToken,
  clearUserToken,
  getClientIdFromToken,
  getClientToken,
  getUserIdFromToken,
  getUserRolesFromToken,
  getUserToken,
  isClienteFromToken,
} from "../api/login-jwt"; // Ajuste o caminho conforme necessário

interface AuthState {
  userId: string | null;
  userRoles: string[] | null;
  clientId: number | null;
  isCliente: boolean | null;
  userToken: string | null;
  clientToken: string | null;
}

interface AuthContextType {
  authState: AuthState;
  login: (authResult: AuthResult) => void;
  logout: () => void;
}

const defaultAuthState: AuthState = {
  userId: null,
  userRoles: null,
  clientId: null,
  isCliente: null,
  userToken: null,
  clientToken: null,
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [authState, setAuthState] = useState<AuthState>(defaultAuthState);
  const navigate = useNavigate();

  const loadAuthData = () => {
    setAuthState({
      userId: getUserIdFromToken(),
      userRoles: getUserRolesFromToken(),
      clientId: getClientIdFromToken(),
      isCliente: isClienteFromToken(),
      userToken: getUserToken(),
      clientToken: getClientToken(),
    });
  };

  useEffect(() => {
    loadAuthData();
  }, []);

  const login = (authResult: AuthResult) => {
    if (authResult) {
      if (authResult.type === "user") {
        const newState: AuthState = {
          ...authState,
          userId: authResult.userId,
          userRoles: authResult.userRoles,
          userToken: authResult.token,
        };
        setAuthState(newState);
        // Persist user roles to localStorage here!
        localStorage.setItem("userRoles", JSON.stringify(authResult.userRoles));
      } else if (authResult.type === "client") {
        const newState: AuthState = {
          ...authState,
          clientId: authResult.clientId,
          isCliente: true,
          clientToken: authResult.token,
        };
        setAuthState(newState);
      }
    }
  };

  const logout = () => {
    clearUserToken();
    clearClientToken();
    localStorage.removeItem("userRoles"); // Clear user roles on logout
    setAuthState(defaultAuthState);
    navigate({ to: "/" });
  };

  const value: AuthContextType = {
    authState,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
