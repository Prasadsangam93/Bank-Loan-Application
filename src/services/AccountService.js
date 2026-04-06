import axios from "axios";

const API_URL = "http://localhost:9092/api/accounts";

class AccountService {

  createAccount(account){
    return axios.post(`${API_URL}/create`, account);
  }

  getAllAccounts(){
    return axios.get(`${API_URL}/getAllAccounts`);
  }

  holdAccount(accountNumber){
    return axios.put(`${API_URL}/hold/${accountNumber}`);
  }

  unholdAccount(accountNumber){
    return axios.put(`${API_URL}/unhold/${accountNumber}`);
  }

  blockAccount(accountNumber){
    return axios.put(`${API_URL}/block/${accountNumber}`);
  }

}

const accountService = new AccountService();
export default accountService;