// frontend/src/components/dashboard/LogoutButton.tsx
import { useNavigate } from "@tanstack/react-router";
import React from "react";
import { clearClientToken, clearUserToken } from "../../api/login-jwt";

type LogoutButtonProps = {
  userType: "user" | "client";
};

const LogoutButton: React.FC<LogoutButtonProps> = ({ userType }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    if (userType === "user") {
      clearUserToken();
    } else {
      clearClientToken();
    }
    navigate({ to: "/login" });
  };

  return (
    <button
      onClick={handleLogout}
      className="w-full p-2 text-left text-white bg-red-600 hover:bg-red-700 rounded mt-4" // Added mt-4 here
    >
      Sair
    </button>
  );
};

export default LogoutButton;
