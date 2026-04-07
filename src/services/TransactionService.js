import axios from "axios";

const API_URL = "http://localhost:9093/api/transactions";

class TransactionService {

createTransaction(data){
return axios.post(`${API_URL}/create`, data);
}

getAll(){
return axios.get(`${API_URL}`);
}

getByAccount(accountNumber){
return axios.get(`${API_URL}/account/${accountNumber}`);
}

}

export default new TransactionService();