// frontend/src/components/dashboard/dashboardAdmin.tsx
import React, { useState, useEffect } from "react";
import logo from "../../assets/logo.png";
import ClientManagement from "./clienteManagement";
import UserManagement from "./userManagement";
import LogoutButton from "./logoutbutton"; // Import the LogoutButton
import {
  getUserRolesFromToken,
  getClientIdFromToken,
  isClienteFromToken,
} from "../../api/login-jwt";

const menuItems = [
  { label: "Criar Usuário", value: "criar-usuario" },
  { label: "Lista de Usuários", value: "lista-usuarios" },
  { label: "Criar Cliente", value: "criar-cliente" },
  { label: "Lista de Clientes", value: "lista-clientes" },
];

const DashboardAdmin: React.FC = () => {
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState<"success" | "error" | "">("");
  const [selectedMenuItem, setSelectedMenuItem] = useState("criar-usuario");
  const [userType, setUserType] = useState<"user" | "client" | null>(null); // Add userType state

  useEffect(() => {
    // Determine user type on component mount
    const roles = getUserRolesFromToken();
    const isClient = isClienteFromToken();

    if (roles && roles.length > 0) {
      setUserType("user");
    } else if (isClient) {
      setUserType("client");
    } else {
      // Handle the case where user type cannot be determined
      //   setUserType(null) // Or redirect to login, or show an error
    }
  }, []); // Empty dependency array ensures this runs only once on mount

  const showMessage = (msg: string, type: "success" | "error") => {
    setMessage(msg);
    setMessageType(type);
    setTimeout(() => {
      setMessage("");
      setMessageType("");
    }, 5000);
  };

  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-6">
        <div className="flex items-center justify-center mb-6">
          <img
            src={logo}
            alt="SupportFlow Logo"
            className="h-14 w-auto mr-2 cursor-pointer"
          />
          <h2 className="text-xl font-bold">Support Flow</h2>
        </div>
        <nav className="mt-auto pt-1">
          <ul className="space-y-2">
            {menuItems.map((item) => (
              <li
                key={item.value}
                className={`p-2 rounded cursor-pointer ${
                  selectedMenuItem === item.value
                    ? "bg-green-600"
                    : "hover:bg-green-700"
                }`}
                onClick={() => setSelectedMenuItem(item.value)}
              >
                {item.label}
              </li>
            ))}
          </ul>
          {/* Conditionally render the LogoutButton based on userType */}
          {userType && <LogoutButton userType={userType} />}
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-6 bg-gray-100 overflow-auto">
        {/* Message display */}
        {message && (
          <div
            className={`mb-4 p-3 rounded ${
              messageType === "success"
                ? "bg-green-100 text-green-700"
                : "bg-red-100 text-red-700"
            }`}
          >
            {message}
          </div>
        )}

        {selectedMenuItem === "criar-usuario" && (
          <UserManagement showMessage={showMessage} displayMode="create" />
        )}
        {selectedMenuItem === "lista-usuarios" && (
          <UserManagement showMessage={showMessage} displayMode="list" />
        )}
        {selectedMenuItem === "criar-cliente" && (
          <ClientManagement showMessage={showMessage} displayMode="create" />
        )}
        {selectedMenuItem === "lista-clientes" && (
          <ClientManagement showMessage={showMessage} displayMode="list" />
        )}
      </main>
    </div>
  );
};

export default DashboardAdmin;
