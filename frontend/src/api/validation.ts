// validations.ts

export const validateEmail = (email: string): string => {
  if (!email) {
    return "O e-mail é obrigatório.";
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    return "Por favor, insira um e-mail válido.";
  }
  return ""; // Sem erros
};

export const validatePassword = (password: string): string => {
  if (!password) {
    return "A senha é obrigatória.";
  }
  if (password.length < 8) {
    return "A senha deve ter pelo menos 8 caracteres.";
  }
  return ""; // Sem erros
};

//Pode-se adicionar mais funções de validação aqui, ex:
// export const validateName = (name: string): string => { ... };

// Interface para facilitar a utilização, e deixar o código mais limpo.
export interface ValidationResult {
  isValid: boolean;
  errors: {
    email?: string;
    password?: string;
    // Outros campos, se houver
  };
}

//Função que usa as funções de validação e retorna um objeto com o resultado
export const validateLoginForm = (
  email: string,
  password: string
): ValidationResult => {
  const errors: ValidationResult["errors"] = {}; //Usa o tipo definido na interface.

  const emailError = validateEmail(email);
  if (emailError) {
    errors.email = emailError;
  }

  const passwordError = validatePassword(password);
  if (passwordError) {
    errors.password = passwordError;
  }

  const isValid = Object.keys(errors).length === 0; //Se não tiver nenhuma chave, não tem erros

  return { isValid, errors };
};
