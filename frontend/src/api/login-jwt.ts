// src/api/login-jwt.ts

import axios, { AxiosError } from "axios";
import { jwtDecode } from "jwt-decode";

interface JWTPayload {
  sub?: string; // User ID
  email?: string;
  roles?: string[];
  Cliente?: boolean; // Client flag
  entityId?: number; // Client ID
  exp: number;
  iat: number;
  [key: string]: any;
}

export type AuthResult =
  | {
      type: "user";
      token: string;
      userId: string;
      userEmail: string;
      userRoles: string[];
      expirationTime: number;
    }
  | {
      type: "client";
      token: string;
      clientId: number;
      expirationTime: number;
    }
  | null;

const login = async (email: string, password: string): Promise<AuthResult> => {
  // USE A RELATIVE URL or an environment variable
  const options = {
    method: "POST",
    url: "http://localhost:8080/api/auth/login", // Use a relative URL!  Or an environment variable.
    headers: { "content-type": "application/json" },
    data: { email: email, password: password },
  };

  try {
    const { data } = await axios.request(options);

    if (!data || !data.jwt) {
      console.error("Token not found in response");
      throw new Error("Token not found in response");
    }

    const token = data.jwt;
    const decodedToken: JWTPayload = jwtDecode(token);
    const expirationTime = decodedToken.exp;

    if (decodedToken.Cliente === true && decodedToken.entityId !== undefined) {
      // Client Login
      const clientId = decodedToken.entityId;
      setAuthToken("client", token); // Centralized token storage
      console.log("Client logged in with ID:", clientId);
      return {
        type: "client",
        token: token,
        clientId: clientId,
        expirationTime: expirationTime,
      };
    } else if (decodedToken.sub && decodedToken.roles) {
      // User Login
      const userId = decodedToken.sub;
      const userEmail = decodedToken.email || userId;
      const userRoles = decodedToken.roles || [];
      setAuthToken("user", token); // Centralized token storage
      console.log("User logged in with ID:", userId, "and roles:", userRoles);
      return {
        type: "user",
        token: token,
        userId: userId,
        userEmail: userEmail,
        userRoles: userRoles,
        expirationTime: expirationTime,
      };
    } else {
      console.error("Invalid token payload:", decodedToken);
      throw new Error(
        "Invalid token payload: Unable to determine entity type."
      );
    }
  } catch (error: any) {
    console.error("Login error:", error);

    if (axios.isAxiosError(error)) {
      const axiosError = error as AxiosError<{ message?: string }>; // Explicitly type the error data
      const message =
        axiosError.response?.data?.message || "Unknown error during login.";
      throw new Error(
        `Login failed with status ${axiosError.response?.status}: ${message}`
      );
    }

    throw new Error(`Login failed: ${error.message}`); // Generic error
  }
};

// Centralized token storage
const setAuthToken = (type: "user" | "client", token: string) => {
  localStorage.setItem(`${type}-token`, token);
};

const getAuthToken = (type: "user" | "client"): string | null => {
  return localStorage.getItem(`${type}-token`);
};

const clearAuthToken = (type: "user" | "client") => {
  localStorage.removeItem(`${type}-token`);
};

// Updated token expiration check
const isTokenExpired = (tokenType: "user" | "client"): boolean => {
  const token = getAuthToken(tokenType);
  if (!token) return true;

  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    const currentTime = Math.floor(Date.now() / 1000);
    return decodedToken.exp < currentTime;
  } catch (error) {
    console.error("Error decoding token:", error);
    return true; // Treat as expired if decoding fails
  }
};

export const isUserTokenExpired = (): boolean => isTokenExpired("user");
export const isClientTokenExpired = (): boolean => isTokenExpired("client");

// Separate functions to get tokens
export const getUserToken = (): string | null => getAuthToken("user");
export const getClientToken = (): string | null => getAuthToken("client");

// Clear tokens
export const clearUserToken = () => clearAuthToken("user");
export const clearClientToken = () => clearAuthToken("client");

// Functions to extract information from the token
export const getUserIdFromToken = (): string | null => {
  const token = getAuthToken("user");
  if (!token) return null;
  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    return decodedToken.sub || null;
  } catch (error) {
    console.error("Error decoding user token:", error);
    return null;
  }
};

export const getUserRolesFromToken = (): string[] | null => {
  const token = getAuthToken("user");
  if (!token) return null;
  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    return decodedToken.roles || [];
  } catch (error) {
    console.error("Error decoding user token:", error);
    return null;
  }
};

export const getClientIdFromToken = (): number | null => {
  const token = getAuthToken("client");
  if (!token) return null;
  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    return decodedToken.entityId || null;
  } catch (error) {
    console.error("Error decoding client token:", error);
    return null;
  }
};

export const isClienteFromToken = (): boolean | null => {
  const token = getAuthToken("client");
  if (!token) return null;
  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    return decodedToken.Cliente || null;
  } catch (error) {
    console.error("Error decoding client token:", error);
    return null;
  }
};

export default login;
