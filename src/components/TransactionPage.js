import React,{useState} from "react";
import TransactionService from "../services/TransactionService";

function TransactionPage(){

const [accountNumber,setAccountNumber] = useState("");
const [transactionType,setTransactionType] = useState("");
const [amount,setAmount] = useState("");

const [transactions,setTransactions] = useState([]);
const [showList,setShowList] = useState(false);

const [savedTransaction,setSavedTransaction] = useState(null);

const [errors,setErrors] = useState({});


// ✅ VALIDATION
const validate = () => {

let err = {};

if(!accountNumber){
err.accountNumber = "Account Number Required";
}

if(!transactionType){
err.transactionType = "Select Transaction Type";
}

if(!amount){
err.amount = "Amount Required";
}
else if(amount <= 0){
err.amount = "Amount must be greater than 0";
}
else if(amount > 1000000){
err.amount = "Amount too large";
}

setErrors(err);
return Object.keys(err).length === 0;
};


// ✅ CREATE TRANSACTION
const createTransaction = (e)=>{

e.preventDefault();

if(!validate()) return;

TransactionService.createTransaction({
accountNumber,
transactionType,
amount
})
.then(res=>{
setSavedTransaction(res.data);
clearForm();
})
.catch(err=>{
// 🔥 IMPORTANT → show backend error (like insufficient balance)
setErrors({
api: err.response?.data || "Transaction failed"
});
});

};


// ✅ SHOW ALL
const loadAllTransactions = () => {

if(showList){
setShowList(false);
return;
}

TransactionService.getAll()
.then(res=>{
setTransactions(res.data);
setShowList(true);
setErrors({});
})
.catch(()=>{
setErrors({api:"Error fetching transactions"});
});

};


// ✅ FETCH BY ACCOUNT
const loadByAccount = () => {

if(!accountNumber){
setErrors({accountNumber:"Enter account number"});
return;
}

TransactionService.getByAccount(accountNumber)
.then(res=>{

if(res.data.length === 0){
setErrors({api:"No transactions found"});
return;
}

setTransactions(res.data);
setShowList(true);
setErrors({});

})
.catch(()=>{
setErrors({api:"Error fetching account transactions"});
});

};


// CLEAR
const clearForm = () => {
setAccountNumber("");
setTransactionType("");
setAmount("");
setErrors({});
};


return(

<div style={container}>

<h2>Transaction Service</h2>

<form style={formStyle} onSubmit={createTransaction}>

<input
placeholder="Account Number"
value={accountNumber}
onChange={(e)=>setAccountNumber(e.target.value)}
style={inputStyle}
/>
<p style={errorStyle}>{errors.accountNumber}</p>

<select
value={transactionType}
onChange={(e)=>setTransactionType(e.target.value)}
style={inputStyle}
>
<option value="">Select Transaction</option>
<option value="CREDIT">CREDIT</option>
<option value="DEBIT">DEBIT</option>
</select>
<p style={errorStyle}>{errors.transactionType}</p>

<input
type="number"
placeholder="Amount"
value={amount}
onChange={(e)=>setAmount(e.target.value)}
style={inputStyle}
/>
<p style={errorStyle}>{errors.amount}</p>

<p style={errorStyle}>{errors.api}</p>

<button style={buttonStyle}>
Save Transaction
</button>

</form>

{/* SAVED */}

{savedTransaction && (
<div>
<h3>Saved Transaction</h3>
<table style={tableStyle}>
<thead>
<tr style={headerStyle}>
<th>ID</th>
<th>Account</th>
<th>Type</th>
<th>Amount</th>
<th>Date</th>
</tr>
</thead>
<tbody>
<tr style={{textAlign:"center"}}>
<td>{savedTransaction.id}</td>
<td>{savedTransaction.accountNumber}</td>
<td>{savedTransaction.transactionType}</td>
<td>{savedTransaction.amount}</td>
<td>{savedTransaction.transactionDate}</td>
</tr>
</tbody>
</table>
</div>
)}

<hr/>

<button style={listBtn} onClick={loadAllTransactions}>
{showList ? "Hide All Transactions" : "Show All Transactions"}
</button>

<button style={listBtn} onClick={loadByAccount}>
Show By Account
</button>

{/* LIST */}

{showList && (
<table style={tableStyle}>
<thead>
<tr style={headerStyle}>
<th>ID</th>
<th>Account</th>
<th>Type</th>
<th>Amount</th>
<th>Date</th>
</tr>
</thead>
<tbody>
{transactions.map(t=>(
<tr key={t.id}>
<td>{t.id}</td>
<td>{t.accountNumber}</td>
<td>{t.transactionType}</td>
<td>{t.amount}</td>
<td>{t.transactionDate}</td>
</tr>
))}
</tbody>
</table>
)}

</div>

);

}


// STYLES
const container={background:"#f4f6f9",padding:"30px"};
const formStyle={background:"white",padding:"20px",width:"400px"};
const inputStyle={width:"100%",padding:"10px",marginTop:"10px"};
const errorStyle={color:"red",fontSize:"12px"};
const buttonStyle={width:"100%",padding:"10px",background:"green",color:"white",marginTop:"10px"};
const listBtn={padding:"10px",background:"blue",color:"white",marginTop:"10px",marginRight:"10px"};
const tableStyle={width:"100%",marginTop:"20px",background:"white"};
const headerStyle={background:"#333",color:"white"};

export default TransactionPage;