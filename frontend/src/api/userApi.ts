// frontend/src/api/userApi.ts

import axios from 'axios';
import { getToken } from '../components/login/login-jwt'; // Import getToken function

interface UserData {
  nome: string;
  email: string;
  telefone: string;
  cpfCnpj: string;
  password?: string; // Make password optional for updates, required for creation
  roles?: string[];    // added roles
}
interface UserDataCreate extends UserData{
  password: string;
}


const BASE_URL = 'http://localhost:10001/api'; // Consistent base URL

// Function to create a user
export const createUser = async (userData: UserDataCreate) => {
    const token = getToken();
    if (!token) {
        throw new Error("User not authenticated");
    }

    const options = {
        method: 'POST',
        url: `${BASE_URL}/users`, // Correct endpoint for user creation
        headers: {
            authorization: `Bearer ${token}`,
            'content-type': 'application/json'
        },
        data: userData
    };
    try {
        const { data } = await axios.request(options);
        return data; // Return the created user data
    } catch (error) {
        if (axios.isAxiosError(error)) {
            if (error.response) {
              throw new Error(`User creation failed with status ${error.response.status}: ${error.response.data}`);
            } else if (error.request) {
              throw new Error("User creation failed: No response received.");
            } else {
               throw new Error("User creation failed: Request setup error: " + error.message)
            }
          }
        throw error;  // Re-throw unknown errors
    }
};



// Function to update a user
export const updateUser = async (userId: string, userData: UserData) => {
  const token = getToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: 'PUT',
    url: `${BASE_URL}/users/${userId}`, // Correct endpoint for user update
    headers: {
      authorization: `Bearer ${token}`,
      'content-type': 'application/json',
    },
    data: userData,
  };

  try {
    const { data } = await axios.request(options);
    return data; // Return the updated user data
  } catch (error) {
        if (axios.isAxiosError(error)) {
            if (error.response) {
              throw new Error(`User update failed with status ${error.response.status}: ${error.response.data}`);
            } else if (error.request) {
              throw new Error("User update failed: No response received.");
            } else {
               throw new Error("User update failed: Request setup error: " + error.message)
            }
          }
        throw error;  // Re-throw unknown errors
  }
};



// Function to get all users
export const getAllUsers = async () => {
  const token = getToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: 'GET',
    url: `${BASE_URL}/users`,  // No userId, to get all users
    headers: {
      authorization: `Bearer ${token}`,
      'content-type': 'application/json',
    },

  };

  try {
    const { data } = await axios.request(options);
     // Return the updated user data
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
        if (error.response) {
          throw new Error(`Get users failed with status ${error.response.status}: ${error.response.data}`);
        } else if (error.request) {
          throw new Error("Get users failed: No response received.");
        } else {
           throw new Error("Get users failed: Request setup error: " + error.message)
        }
      }
    throw error; // Re-throw unknown errors
  }
};


// Function to get user by ID
export const getUserById = async (userId:string) => {
  const token = getToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: 'GET',
    url: `${BASE_URL}/users/${userId}`,
    headers: {
      authorization: `Bearer ${token}`,
      'content-type': 'application/json',
    },

  };

  try {
    const { data } = await axios.request(options);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
        if (error.response) {
          throw new Error(`Get user failed with status ${error.response.status}: ${error.response.data}`);
        } else if (error.request) {
          throw new Error("Get user failed: No response received.");
        } else {
           throw new Error("Get user failed: Request setup error: " + error.message)
        }
      }
    throw error;  // Re-throw unknown errors

  }
};

// Function to delete user by id
export const deleteUserById = async (userId:string) => {
  const token = getToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: 'DELETE',
    url: `${BASE_URL}/users/${userId}`,
    headers: {
      authorization: `Bearer ${token}`,
      'content-type': 'application/json',
    },

  };

  try {
    const { data } = await axios.request(options);
    return data; // normally returns a sucess message or NoContent
  } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response) {
          throw new Error(`Delete user failed with status ${error.response.status}: ${error.response.data}`);
        } else if (error.request) {
          throw new Error("Delete user failed: No response received.");
        } else {
           throw new Error("Delete user failed: Request setup error: " + error.message)
        }
      }
    throw error; // Re-throw unknown errors
  }
}
