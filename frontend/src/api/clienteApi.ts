// src/api/clientApi.ts

import axios from "axios";
import { getUserToken } from "./login-jwt";

interface ClientData {
  nome: string;
  email: string;
  telefone?: string;
  cpfCnpj?: string;
}

interface ClientDataCreate extends ClientData {
  senha?: string;
}

const BASE_URL = "http://localhost:8080/api";

const DEFAULT_EMPRESA_NOME = "SuportFlow"; // Define the default company name

// Function to create a client
export const createClient = async (clientData: ClientDataCreate) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "POST",
    url: `${BASE_URL}/clientes`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
    data: {
      ...clientData,
      telefone: clientData.telefone === "" ? null : clientData.telefone,
      cpfCnpj: clientData.cpfCnpj === "" ? null : clientData.cpfCnpj,
      empresaNome: DEFAULT_EMPRESA_NOME,
    },
  };

  try {
    const { data } = await axios.request(options);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(
        `Client creation failed with status ${error.response?.status}: ${error.response?.data}`
      );
    }
    throw error;
  }
};

// Function to update a client
export const updateClient = async (
  clientId: string,
  clientData: ClientData
) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "PUT",
    url: `${BASE_URL}/clientes/${clientId}`,
    headers: {
      authorization: `Bearer ${token}`,
      "content-type": "application/json",
    },
    data: {
      ...clientData,
      telefone: clientData.telefone === "" ? null : clientData.telefone,
      cpfCnpj: clientData.cpfCnpj === "" ? null : clientData.cpfCnpj,
      empresaNome: DEFAULT_EMPRESA_NOME, // Always send "SuportFlow"
    },
  };

  try {
    const { data } = await axios.request(options);
    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(
        `Client update failed with status ${error.response?.status}: ${error.response?.data}`
      );
    }
    throw error;
  }
};

// Function to get all clients
export const getAllClients = async () => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "GET",
    url: `${BASE_URL}/clientes`,
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
      throw new Error(
        `Get clients failed with status ${error.response?.status}: ${error.response?.data}`
      );
    }
    throw error;
  }
};

// Function to get client by ID
export const getClientById = async (clientId: string) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "GET",
    url: `${BASE_URL}/clientes/${clientId}`,
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
      throw new Error(
        `Get client failed with status ${error.response?.status}: ${error.response?.data}`
      );
    }
    throw error;
  }
};

// Function to delete client by id
export const deleteClientById = async (clientId: string) => {
  const token = getUserToken();
  if (!token) {
    throw new Error("User not authenticated");
  }

  const options = {
    method: "DELETE",
    url: `${BASE_URL}/clientes/${clientId}`,
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
      throw new Error(
        `Delete client failed with status ${error.response?.status}: ${error.response?.data}`
      );
    }
    throw error;
  }
};
