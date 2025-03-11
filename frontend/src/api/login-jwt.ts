import axios from "axios";
import { jwtDecode } from "jwt-decode";

interface JWTPayload {
  sub: string;
  email?: string;
  roles: string[];
  exp: number;
  iat: number;
  [key: string]: any;
}

// Changed to accept email and password as arguments
const login = async (email: string, password: string) => {
  const options = {
    method: "POST",
    url: "http://localhost:10001/api/auth/login", // Ensure this URL is correct
    headers: { "content-type": "application/json" },
    data: { email: email, password: password }, // Use the passed-in email and password
  };

  try {
    const { data } = await axios.request(options);
    console.log(data);

    if (data && data.jwt) {
      const token = data.jwt;
      const decodedToken: JWTPayload = jwtDecode(token);

      console.log("Decoded Token:", decodedToken);
      localStorage.setItem("jwtToken", token);

      const userId = decodedToken.sub;
      const userEmail = decodedToken.email || userId;
      const userPermissions = decodedToken.roles;
      const expirationTime = decodedToken.exp;

      console.log("User ID:", userId);
      console.log("User Email:", userEmail);
      console.log("User Permissions:", userPermissions);
      console.log("Expiration Timestamp:", expirationTime);
      console.log("Expiration Date:", new Date(expirationTime * 1000));

      if (userPermissions.includes("ADMIN")) {
        console.log("User is an admin");
      }

      return {
        token,
        userId,
        userEmail,
        userPermissions,
        expirationTime,
      };
    } else {
      console.error("Token not found in response");
      throw new Error("Token not found in response"); // Consistent error message
    }
  } catch (error) {
    console.error(error);
    // Throw a more specific error if possible
    if (axios.isAxiosError(error)) {
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        throw new Error(
          `Login failed with status ${error.response.status}: ${error.response.data}`
        );
      } else if (error.request) {
        // The request was made but no response was received
        throw new Error("Login failed: No response received from server.");
      } else {
        // Something happened in setting up the request that triggered an Error
        throw new Error("Login failed: Request setup error: " + error.message);
      }
    }
    throw error; // Re-throw other errors
  }
};

export const isTokenExpired = (): boolean => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    return true;
  }
  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    const currentTime = Math.floor(Date.now() / 1000);
    return decodedToken.exp < currentTime;
  } catch (error) {
    console.error("Error decoding token:", error);
    return true;
  }
};

export const getToken = (): string | null => {
  return localStorage.getItem("jwtToken");
};

export const getUserPermissions = (): string[] | null => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    return null;
  }
  try {
    const decodedToken: JWTPayload = jwtDecode(token);
    return decodedToken.roles;
  } catch (error) {
    console.error("Error decoding token", error);
    return null;
  }
};

export const clearToken = () => {
  localStorage.removeItem("jwtToken");
};

export default login;
