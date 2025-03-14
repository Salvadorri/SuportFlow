// src/api/userApi.ts

import axios from "axios";
import { getUserToken } from "./login-jwt";

interface UserData {
  nome: string;
  email: string;
  telefone?: string;
  cpfCnpj?: string;
  password?: string;
  role?: string; // Role is optional for updates
}

interface UserDataCreate extends Omit<UserData, "password"> {
  password?: string; // Password is required for creation, optional for update.
  role: string; // Role is REQUIRED for creation
}

const BASE_URL = "http://localhost:8080/api";

// Function to create a user
export const createUser = async (userData: UserDataCreate) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  // Ensure role is provided and is a string
  if (!userData.role || typeof userData.role !== "string") {
    throw new Error("Role is required and must be a string for user creation.");
  }

  const options = {
    method: "POST",
    url: `${BASE_URL}/users`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
    data: userData, // No need to clean, ensure all data is valid instead
  };

  try {
    const { data } = await axios.request(options);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(
          `User creation failed with status ${error.response.status}: ${error.response.data}`
        );
      } else if (error.request) {
        throw new Error("User creation failed: No response received.");
      } else {
        throw new Error(
          "User creation failed: Request setup error: " + error.message
        );
      }
    }
    throw error;
  }
};

// Function to update a user
export const updateUser = async (userId: string, userData: UserData) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  // Remover campos undefined ou vazios
  const cleanedData = Object.entries(userData).reduce((acc, [key, value]) => {
    if (value !== undefined && value !== "") {
      acc[key] = value;
    }
    return acc;
  }, {} as Record<string, any>);

  const options = {
    method: "PUT",
    url: `${BASE_URL}/users/${userId}`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
    data: cleanedData,
  };

  try {
    const { data } = await axios.request(options);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(
          `User update failed with status ${error.response.status}: ${error.response.data}`
        );
      } else if (error.request) {
        throw new Error("User update failed: No response received.");
      } else {
        throw new Error(
          "User update failed: Request setup error: " + error.message
        );
      }
    }
    throw error;
  }
};

// Function to get all users
export const getAllUsers = async () => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "GET",
    url: `${BASE_URL}/users`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
  };

  try {
    const { data } = await axios.request(options);
    // Transforma a resposta do servidor para garantir que role seja sempre uma string
    return data.map((user: any) => ({
      ...user,
      role: user.role || user.roles?.[0] || "ATENDENTE", // Compatibilidade com backends que usam roles ou role
    }));
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(
          `Get users failed with status ${error.response.status}: ${error.response.data}`
        );
      } else if (error.request) {
        throw new Error("Get users failed: No response received.");
      } else {
        throw new Error(
          "Get users failed: Request setup error: " + error.message
        );
      }
    }
    throw error;
  }
};

// Function to get user by ID
export const getUserById = async (userId: string) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "GET",
    url: `${BASE_URL}/users/${userId}`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
  };

  try {
    const { data } = await axios.request(options);
    // Garantir que o usuário tenha uma role única
    return {
      ...data,
      role: data.role || data.roles?.[0] || "ATENDENTE", // Compatibilidade com backends que usam roles ou role
    };
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(
          `Get user failed with status ${error.response.status}: ${error.response.data}`
        );
      } else if (error.request) {
        throw new Error("Get user failed: No response received.");
      } else {
        throw new Error(
          "Get user failed: Request setup error: " + error.message
        );
      }
    }
    throw error;
  }
};

// Function to delete user by id
export const deleteUserById = async (userId: string) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "DELETE",
    url: `${BASE_URL}/users/${userId}`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
  };

  try {
    const { data } = await axios.request(options);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(
          `Delete user failed with status ${error.response.status}: ${error.response.data}`
        );
      } else if (error.request) {
        throw new Error("Delete user failed: No response received.");
      } else {
        throw new Error(
          "Delete user failed: Request setup error: " + error.message
        );
      }
    }
    throw error;
  }
};

// Função para obter os papéis disponíveis
export const getAvailableRoles = () => {
  return ["ADMIN", "GERENTE", "ATENDENTE"];
};
