import axios from "axios";

export const loginUser = async (email: string, password: string) => {
  const response = await axios.post(
    "http://localhost:8080/api/auth/login",
    {
      email,
       motDePasse: password,
    }
  );

  return response.data;
};
export const getUsers = async () => {
  const token = localStorage.getItem("token");

  const response = await axios.get(
    "http://localhost:8080/api/adminDash",
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );

  return response.data;
};