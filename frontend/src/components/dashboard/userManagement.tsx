// src/components/dashboard/UserManagement.tsx
import React, { useEffect, useState } from "react";
import {
  createUser,
  deleteUserById,
  getAllUsers,
  getUserById,
  updateUser,
} from "../../api/userApi";
import UserForm from "./userForm";

interface User {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  cpfCnpj?: string;
  roles: string[];
}

interface UserManagementProps {
  showMessage: (message: string, type: "success" | "error") => void;
  displayMode: "create" | "list";
}

// Definição das roles disponíveis
const AVAILABLE_ROLES = ["GERENTE", "ATENDENTE"];

const UserManagement: React.FC<UserManagementProps> = ({
  showMessage,
  displayMode,
}) => {
  const [users, setUsers] = useState<User[]>([]); // Corrected type annotation
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // User form state
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [cpfCnpj, setCpfCnpj] = useState("");
  const [password, setPassword] = useState("");
  const [selectedRole, setSelectedRole] = useState<string>("ATENDENTE");

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

  const resetForm = () => {
    setName("");
    setEmail("");
    setPhone("");
    setCpfCnpj("");
    setPassword("");
    setSelectedRole("ATENDENTE");
  };

  // const handleSelectRole = (role: string) => {
  //   setSelectedRole(role);
  // };

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
        role: selectedRole, // Adicionado para satisfazer o tipo UserDataCreate
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
    return true;
  };

  const filteredUsers = users.filter(
    (user) =>
      user.nome?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.cpfCnpj?.includes(searchTerm)
  );

  return (
    <div>
      {displayMode === "create" && (
        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-4">Criar Usuário</h2>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <UserForm
              name={name}
              setName={setName}
              email={email}
              setEmail={setEmail}
              phone={phone}
              setPhone={setPhone}
              cpfCnpj={cpfCnpj}
              setCpfCnpj={setCpfCnpj}
              password={password}
              setPassword={setPassword}
              selectedRole={selectedRole}
              setSelectedRole={setSelectedRole}
              isLoading={isLoading}
              handleSubmit={handleCreateUser}
              availableRoles={AVAILABLE_ROLES}
            />
          </div>
        </div>
      )}

      {displayMode === "list" && (
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
                            {user.id !== "1" && String(user.id) !== "1" && (
                              <button
                                onClick={() => handleSelectUser(user.id)}
                                className="text-blue-600 hover:text-blue-800 font-medium transition-colors"
                              >
                                Editar
                              </button>
                            )}
                            {user.id !== "1" && String(user.id) !== "1" && (
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
      )}

      {editMode && (
        <div className="mb-8">
          <h2 className="text-2xl font-semibold mb-4">Editar Usuário</h2>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <UserForm
              name={name}
              setName={setName}
              email={email}
              setEmail={setEmail}
              phone={phone}
              setPhone={setPhone}
              cpfCnpj={cpfCnpj}
              setCpfCnpj={setCpfCnpj}
              password={password}
              setPassword={setPassword}
              selectedRole={selectedRole}
              setSelectedRole={setSelectedRole}
              isLoading={isLoading}
              handleCancelEdit={handleCancelEdit}
              handleSubmit={handleUpdateUser}
              isEdit={true}
              availableRoles={AVAILABLE_ROLES}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default UserManagement;
