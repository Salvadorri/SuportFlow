import { Link } from "@tanstack/react-router";
import React, { useEffect, useState } from "react";
import {
  createUser,
  deleteUserById,
  getAllUsers,
  getUserById,
  updateUser,
} from "../../api/userApi";

// Definindo a interface para o usuário
interface User {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  cpfCnpj?: string;
  roles: string[];
}

// Definição das roles disponíveis
const AVAILABLE_ROLES = ["ADMIN", "GERENTE", "ATENDENTE"];

const UserManagement: React.FC = () => {
  // User form state
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [cpfCnpj, setCpfCnpj] = useState("");
  const [password, setPassword] = useState("");
  const [selectedRole, setSelectedRole] = useState<string>("ATENDENTE");
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState<"success" | "error" | "">("");

  // Users list state
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // Load all users on component mount
  useEffect(() => {
    fetchAllUsers();
  }, []);

  const fetchAllUsers = async () => {
    setIsLoading(true);
    try {
      const allUsers = await getAllUsers();
      setUsers(allUsers || []);
    } catch (error: any) {
      showMessage(`Erro ao carregar usuários: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const showMessage = (msg: string, type: "success" | "error") => {
    setMessage(msg);
    setMessageType(type);
    // Auto-clear message after 5 seconds
    setTimeout(() => {
      setMessage("");
      setMessageType("");
    }, 5000);
  };

  const resetForm = () => {
    setName("");
    setEmail("");
    setPhone("");
    setCpfCnpj("");
    setPassword("");
    setSelectedRole("ATENDENTE");
  };

  const handleSelectRole = (role: string) => {
    setSelectedRole(role);
  };

  const handleCreateUser = async () => {
    if (!validateForm()) return;

    setIsLoading(true);
    try {
      const newUser = {
        nome: name.trim(),
        email: email.trim(),
        telefone: phone.trim() || undefined,
        cpfCnpj: cpfCnpj.trim() || undefined,
        password: password,
        roles: [selectedRole],
      };
      await createUser(newUser);
      showMessage(`Usuário criado com sucesso!`, "success");
      resetForm();
      fetchAllUsers();
    } catch (error: any) {
      showMessage(`Erro ao criar usuário: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleUpdateUser = async () => {
    if (!selectedUser || !validateForm(true)) return;

    setIsLoading(true);
    try {
      const updatedUserData = {
        nome: name.trim(),
        email: email.trim(),
        telefone: phone.trim() || undefined,
        cpfCnpj: cpfCnpj.trim() || undefined,
        roles: [selectedRole],
      };

      await updateUser(selectedUser.id, updatedUserData);
      showMessage(`Usuário atualizado com sucesso!`, "success");
      setSelectedUser(null);
      setEditMode(false);
      resetForm();
      fetchAllUsers();
    } catch (error: any) {
      showMessage(`Erro ao atualizar usuário: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteUser = async (userId: string) => {
    if (!confirm("Tem certeza que deseja excluir este usuário?")) return;

    setIsLoading(true);
    try {
      await deleteUserById(userId);
      showMessage(`Usuário excluído com sucesso!`, "success");
      setSelectedUser(null);
      fetchAllUsers();
    } catch (error: any) {
      showMessage(`Erro ao excluir usuário: ${error.message}`, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const handleSelectUser = async (userId: string) => {
    setIsLoading(true);
    try {
      const user = await getUserById(userId);
      setSelectedUser(user);
      setName(user.nome || "");
      setEmail(user.email || "");
      setPhone(user.telefone || "");
      setCpfCnpj(user.cpfCnpj || "");
      setSelectedRole(user.roles?.[0] || "ATENDENTE");
      setEditMode(true);
    } catch (error: any) {
      showMessage(
        `Erro ao buscar detalhes do usuário: ${error.message}`,
        "error"
      );
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setSelectedUser(null);
    setEditMode(false);
    resetForm();
  };

  const validateForm = (isEdit = false) => {
    if (!name.trim()) {
      showMessage("Nome é obrigatório", "error");
      return false;
    }
    if (!email.trim() || !/\S+@\S+\.\S+/.test(email)) {
      showMessage("Email inválido", "error");
      return false;
    }
    if (!isEdit && !password) {
      showMessage("Senha é obrigatória para novos usuários", "error");
      return false;
    }
    if (!selectedRole) {
      showMessage("Selecione uma função", "error");
      return false;
    }
    return true;
  };

  const filteredUsers = users.filter(
    (user) =>
      user.nome?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.cpfCnpj?.includes(searchTerm)
  );

  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white p-5">
        <h1 className="text-2xl font-bold mb-6">User Management</h1>
        <nav>
          <ul>
            <li className="mb-4 font-semibold">Menu:</li>
            <li className="mb-2 hover:bg-gray-700 p-2 rounded">
              <Link
                to="/dashboard"
                className="text-blue-400 block w-full h-full"
              >
                Dashboard
              </Link>
            </li>
            <li className="mb-2 bg-gray-700 p-2 rounded">
              <Link to="#" className="text-blue-400">
                Usuários
              </Link>
            </li>
          </ul>
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

        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-4">
            {editMode ? "Editar Usuário" : "Criar Usuário"}
          </h2>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <form
              className="space-y-4"
              onSubmit={(e) => {
                e.preventDefault();
                editMode ? handleUpdateUser() : handleCreateUser();
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

              {!editMode && (
                <div>
                  <label className="block text-gray-700 mb-2">Senha *</label>
                  <input
                    type="password"
                    className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
                    placeholder="Senha"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required={!editMode}
                  />
                </div>
              )}

              {/* Role Selection */}
              <div>
                <label className="block text-gray-700 mb-2">Função *</label>
                <div className="flex flex-wrap gap-2">
                  {AVAILABLE_ROLES.map((role) => (
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
                    isLoading
                      ? "bg-gray-400"
                      : "bg-green-500 hover:bg-green-600"
                  } text-white rounded-lg transition-colors duration-200 font-medium`}
                >
                  {isLoading
                    ? "Processando..."
                    : editMode
                    ? "Atualizar"
                    : "Salvar"}
                </button>
                {editMode && (
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
          </div>
        </div>

        {/* Users List */}
        <div>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-semibold">Lista de Usuários</h2>
            <input
              type="text"
              placeholder="Pesquisar usuários..."
              className="p-2 border rounded-lg focus:ring-2 focus:ring-blue-500"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <div className="bg-white shadow-lg rounded-lg overflow-hidden">
            {isLoading && (
              <div className="p-4 text-center text-gray-500">
                <div className="animate-pulse flex justify-center">
                  <div className="h-5 w-24 bg-gray-200 rounded"></div>
                </div>
                <p className="mt-2">Carregando usuários...</p>
              </div>
            )}

            {!isLoading && (
              <table className="w-full border-collapse">
                <thead>
                  <tr className="bg-gray-100 text-left border-b border-gray-200">
                    <th className="p-4 font-semibold text-gray-700">Nome</th>
                    <th className="p-4 font-semibold text-gray-700">Email</th>
                    <th className="p-4 font-semibold text-gray-700">Função</th>
                    <th className="p-4 font-semibold text-gray-700">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredUsers.length > 0 ? (
                    filteredUsers.map((user) => (
                      <tr
                        key={user.id}
                        className="border-b hover:bg-gray-50 transition-colors"
                      >
                        <td className="p-4">{user.nome}</td>
                        <td className="p-4">{user.email}</td>
                        <td className="p-4">
                          <span className="px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800">
                            {user.roles?.[0] || ""}
                          </span>
                        </td>
                        <td className="p-4">
                          <div className="flex space-x-3">
                            {user.id !== 1 && String(user.id) !== "1" && (
                              <button
                                onClick={() => handleSelectUser(user.id)}
                                className="text-blue-600 hover:text-blue-800 font-medium transition-colors"
                              >
                                Editar
                              </button>
                            )}
                            {user.id !== 1 && String(user.id) !== "1" && (
                              <button
                                onClick={() => handleDeleteUser(user.id)}
                                className="text-red-600 hover:text-red-800 font-medium transition-colors"
                              >
                                Excluir
                              </button>
                            )}
                          </div>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={4} className="p-6 text-center text-gray-500">
                        {users.length === 0 ? (
                          <div>
                            <p className="text-lg mb-2">
                              Nenhum usuário encontrado
                            </p>
                            <p className="text-sm">
                              Adicione um novo usuário usando o formulário
                              acima.
                            </p>
                          </div>
                        ) : (
                          "Nenhum usuário corresponde à pesquisa."
                        )}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default UserManagement;
