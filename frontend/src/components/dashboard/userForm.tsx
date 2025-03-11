// src/components/dashboard/UserForm.tsx
import React from "react";

interface UserFormProps {
  name: string;
  setName: (name: string) => void;
  email: string;
  setEmail: (email: string) => void;
  phone: string;
  setPhone: (phone: string) => void;
  cpfCnpj: string;
  setCpfCnpj: (cpfCnpj: string) => void;
  password?: string;
  setPassword?: (password: string) => void;
  selectedRole: string;
  setSelectedRole: (role: string) => void;
  isLoading: boolean;
  handleSubmit: () => void;
  handleCancelEdit?: () => void;
  isEdit?: boolean;
  availableRoles: string[];
}

const UserForm: React.FC<UserFormProps> = ({
  name,
  setName,
  email,
  setEmail,
  phone,
  setPhone,
  cpfCnpj,
  setCpfCnpj,
  password,
  setPassword,
  selectedRole,
  setSelectedRole,
  isLoading,
  handleSubmit,
  handleCancelEdit,
  isEdit = false,
  availableRoles,
}) => {
  const handleSelectRole = (role: string) => {
    setSelectedRole(role);
  };

  return (
    <form
      className="space-y-4"
      onSubmit={(e) => {
        e.preventDefault();
        handleSubmit();
      }}
    >
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-gray-700 mb-2">Nome *</label>
          <input
            type="text"
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
            placeholder="Nome completo"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="block text-gray-700 mb-2">Email *</label>
          <input
            type="email"
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-gray-700 mb-2">Telefone</label>
          <input
            type="text"
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
            placeholder="Telefone (opcional)"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
        </div>
        <div>
          <label className="block text-gray-700 mb-2">CPF/CNPJ</label>
          <input
            type="text"
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
            placeholder="CPF ou CNPJ (opcional)"
            value={cpfCnpj}
            onChange={(e) => setCpfCnpj(e.target.value)}
          />
        </div>
      </div>

      {!isEdit && password !== undefined && setPassword !== undefined && (
        <div>
          <label className="block text-gray-700 mb-2">Senha *</label>
          <input
            type="password"
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
      )}

      {/* Role Selection */}
      <div>
        <label className="block text-gray-700 mb-2">Função *</label>
        <div className="flex flex-wrap gap-2">
          {availableRoles.map((role) => (
            <div
              key={role}
              onClick={() => handleSelectRole(role)}
              className={`cursor-pointer px-4 py-2 rounded-full transition-colors text-sm font-medium ${
                selectedRole === role
                  ? "bg-blue-600 text-white"
                  : "bg-gray-200 text-gray-700 hover:bg-gray-300"
              }`}
            >
              {role}
            </div>
          ))}
        </div>
      </div>

      <div className="mt-6 flex gap-4">
        <button
          type="submit"
          disabled={isLoading}
          className={`px-6 py-2 ${
            isLoading ? "bg-gray-400" : "bg-green-500 hover:bg-green-600"
          } text-white rounded-lg transition-colors duration-200 font-medium`}
        >
          {isLoading ? "Processando..." : isEdit ? "Atualizar" : "Salvar"}
        </button>
        {handleCancelEdit && (
          <button
            type="button"
            disabled={isLoading}
            className="px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors duration-200 font-medium"
            onClick={handleCancelEdit}
          >
            Cancelar
          </button>
        )}
      </div>
    </form>
  );
};

export default UserForm;