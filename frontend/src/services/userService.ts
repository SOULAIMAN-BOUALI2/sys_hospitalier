import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

export const createUser = async (form: any) => {
  const token = localStorage.getItem("token");

  return axios.post(`${BASE_URL}/create-user`, form, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const deleteUser = async (email: string) => {
  const token = localStorage.getItem("token");

  return axios.delete(`${BASE_URL}/delete-user/${email}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const updateUser = async (id: number, data: any) => {
  const token = localStorage.getItem("token");

  return axios.put(`${BASE_URL}/update-user/${id}`, data, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const updateUserPage = async (email: string) => {

  const token = localStorage.getItem("token");

  return axios.get(`${BASE_URL}/user/${email}`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
};