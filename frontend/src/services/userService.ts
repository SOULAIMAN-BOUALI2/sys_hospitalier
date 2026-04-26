import axios from "axios";

export const createUser = async (form: any) => {
  const token = localStorage.getItem("token");

  console.log("create user called");
  const response = await axios.post(
    "http://localhost:8080/api/create-user",
    form,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );

  return response.data;
};