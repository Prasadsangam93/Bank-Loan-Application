import axios from "axios";

const API_URL = "http://localhost:9091/api/customers";

class CustomerService {

  // GET ALL
  getCustomers(){
    return axios.get(API_URL);
  }

  // CREATE
  createCustomer(customer){
    return axios.post(`${API_URL}/register`, customer);
  }

  // UPDATE
  updateCustomer(id, customer){
    return axios.put(`${API_URL}/${id}`, customer);
  }

  // DELETE
  deleteCustomer(id){
    return axios.delete(`${API_URL}/${id}`);
  }

  // GET BY ID
  getCustomerById(id){
    return axios.get(`${API_URL}/${id}`);
  }

}

export default new CustomerService();