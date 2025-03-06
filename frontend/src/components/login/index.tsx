import React, { useState } from 'react';
import { validateLoginForm, ValidationResult } from "./validation.ts"; // Importe as funções

function Index() {

  {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState<ValidationResult["errors"]>({}); //Usa o tipo da interface
    const [rememberMe, setRememberMe] = useState(false);
  
    const handleSubmit = (event: React.FormEvent) => {
      event.preventDefault();
  
      const validationResult = validateLoginForm(email, password);
  
      if (validationResult.isValid) {
          // Lógica de envio do formulário
          console.log('Formulário válido. Dados:', { email, password, rememberMe });
      } else {
          setErrors(validationResult.errors); //Atualiza o estado com os erros
          console.log("Formulário inválido")
      }
    };
  
  
      const handleRememberMeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
          setRememberMe(event.target.checked);
      };
  
  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-900">
      <div className="w-full max-w-md p-8 space-y-8 bg-gray-800 rounded-xl shadow-2xl">
        <h2 className="text-3xl font-bold text-center text-white">SuportFlowAI</h2>

        <form className="space-y-6" action="#" method="POST">
          <div>
            <label
              htmlFor="email"
              className="block mb-2 text-sm font-medium text-gray-300"
            >
              E-mail
            </label>
            <input
              type="email"
              id="email"
              className="bg-gray-700 border border-gray-600 text-white text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
              placeholder="seu@email.com"
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
                // Limpa o erro quando o usuário começa a digitar novamente
                if (errors.email) {
                  setErrors(prevErrors => ({ ...prevErrors, email: '' }));
                }
              }}
              required
            />
            {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email}</p>}
          </div>

          <div>
            <label htmlFor="password" className="login-texto-senha">
              Senha
            </label>
            <input
              type="password"
              id="password"
              className="login-campo-senha"
              placeholder="••••••••"
              value={password}
               onChange={(e) => {
                setPassword(e.target.value);
                // Limpa o erro quando o usuário começa a digitar novamente
                if (errors.password) {
                   setErrors(prevErrors => ({ ...prevErrors, password: '' }));
                }
              }}
              required
            />
            {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password}</p>}
          </div>

          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <input
                id="remember-me"
                type="checkbox"
                className="w-4 h-4 text-blue-600 bg-gray-700 rounded border-gray-600 focus:ring-blue-500 focus:ring-2"
              />
              <label
                htmlFor="remember-me"
                className="ml-2 text-sm font-medium text-gray-300"
              >
                Lembrar de mim
              </label>
            </div>

            <a
              href="#"
              className="text-sm font-medium text-blue-500 hover:underline"
            >
              Esqueceu a senha?
            </a>
          </div>
          <button
            type="submit"
            className="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            Entrar
          </button>
        </form>
      </div>
    </div>
  );
}
}

export default Index;
