// src/components/dashboard/ClientForm.tsx
import React from "react";

interface ClientFormProps {
  name: string;
  setName: (name: string) => void;
  email: string;
  setEmail: (email: string) => void;
  phone: string;
  setPhone: (phone: string) => void;
  cpfCnpj: string;
  setCpfCnpj: (cpfCnpj: string) => void;
  senha?: string;
  setSenha?: (senha: string) => void;
  isLoading: boolean;
  handleSubmit: () => void;
  handleCancelEdit?: () => void;
  isEdit?: boolean;
}

const ClientForm: React.FC<ClientFormProps> = ({
  name,
  setName,
  email,
  setEmail,
  phone,
  setPhone,
  cpfCnpj,
  setCpfCnpj,
  senha,
  setSenha,
  isLoading,
  handleSubmit,
  handleCancelEdit,
  isEdit = false,
}) => {
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

      {!isEdit && senha !== undefined && setSenha !== undefined && (
        <div>
          <label className="block text-gray-700 mb-2">Senha *</label>
          <input
            type="password"
            className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />
        </div>
      )}

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

export default ClientForm;
