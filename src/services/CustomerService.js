import axios from "axios";

const API_URL = "http://localhost:9091/api/customers";

class CustomerService {

  getCustomers(){
    return axios.get(API_URL);
  }

  createCustomer(customer){
    return axios.post(API_URL + "/register", customer);
  }

  updateCustomer(id, customer){
    return axios.put(API_URL + "/" + id, customer);
  }

  deleteCustomer(id){
    return axios.delete(API_URL + "/" + id);
  }

}

const customerService = new CustomerService();

export default customerService;