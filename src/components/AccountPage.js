import React,{useState} from "react";
import AccountService from "../services/AccountService";

function AccountPage(){

const [customerId,setCustomerId] = useState("");
const [accountType,setAccountType] = useState("");
const [balance,setBalance] = useState("");

const [accounts,setAccounts] = useState([]);
const [savedAccount,setSavedAccount] = useState(null);

const [showList,setShowList] = useState(false);

const [error,setError] = useState("");

const validate = () =>{

if(!customerId){
setError("Customer ID required");
return false;
}

if(!/^[0-9]+$/.test(customerId)){
setError("Customer ID must be number");
return false;
}

if(!accountType){
setError("Select account type");
return false;
}

if(!balance){
setError("Balance required");
return false;
}

if(balance < 100){
setError("Minimum balance 100");
return false;
}

setError("");
return true;

};

const createAccount = (e)=>{

e.preventDefault();

if(!validate()) return;

const data={
customerId,
accountType,
balance
};

AccountService.createAccount(data)

.then(res=>{
setSavedAccount(res.data);
setCustomerId("");
setAccountType("");
setBalance("");
})

.catch(err=>{
if(err.response){
setError(err.response.data);
}else{
setError("Server error");
}
});

};

const toggleAccounts = ()=>{

if(showList){
setShowList(false);
return;
}

AccountService.getAllAccounts()
.then(res=>{
setAccounts(res.data);
setShowList(true);
});

};

const refreshAccounts = ()=>{

AccountService.getAllAccounts()
.then(res=>{
setAccounts(res.data);
});

};

const holdAccount=(accNo)=>{
AccountService.holdAccount(accNo).then(()=>refreshAccounts());
};

const unholdAccount=(accNo)=>{
AccountService.unholdAccount(accNo).then(()=>refreshAccounts());
};

const blockAccount=(accNo)=>{
AccountService.blockAccount(accNo).then(()=>refreshAccounts());
};

return(

<div style={container}>

<h2>Account Create</h2>

<form style={formStyle} onSubmit={createAccount}>

<input
placeholder="Customer ID"
value={customerId}
onChange={(e)=>setCustomerId(e.target.value)}
style={inputStyle}
/>

<select
value={accountType}
onChange={(e)=>setAccountType(e.target.value)}
style={inputStyle}
>

<option value="">Select Account Type</option>
<option value="SAVINGS">SAVINGS</option>
<option value="CURRENT">CURRENT</option>
<option value="SALARY">SALARY</option>

</select>

<input
placeholder="Balance"
value={balance}
onChange={(e)=>setBalance(e.target.value)}
style={inputStyle}
/>

{error &&(
<p style={errorStyle}>{error}</p>
)}

<button style={createButton}>
Create Account
</button>

</form>

{/* Saved Account */}

{savedAccount &&(

<div>

<h3>Saved Account</h3>

<table style={tableStyle}>

<thead>

<tr style={headerStyle}>
<th>ID</th>
<th>Account</th>
<th>Customer</th>
<th>Type</th>
<th>Balance</th>
<th>Status</th>
</tr>

</thead>

<tbody>

<tr style={{textAlign:"center"}}>

<td>{savedAccount.id}</td>
<td>{savedAccount.accountNumber}</td>
<td>{savedAccount.customerId}</td>
<td>{savedAccount.accountType}</td>
<td>{savedAccount.balance}</td>
<td style={{color:"green"}}>{savedAccount.status}</td>

</tr>

</tbody>

</table>

</div>

)}

<hr/>

<button style={listButton} onClick={toggleAccounts}>
{showList ? "Hide Accounts" : "Show Accounts"}
</button>

{showList &&(

<table style={tableStyle}>

<thead>

<tr style={headerStyle}>
<th>ID</th>
<th>Account</th>
<th>Customer</th>
<th>Type</th>
<th>Balance</th>
<th>Status</th>
<th>Action</th>
</tr>

</thead>

<tbody>

{accounts.map(acc=>(

<tr key={acc.id} style={{textAlign:"center"}}>

<td>{acc.id}</td>
<td>{acc.accountNumber}</td>
<td>{acc.customerId}</td>
<td>{acc.accountType}</td>
<td>{acc.balance}</td>

<td style={{
color:
acc.status==="ACTIVE"?"green":
acc.status==="HOLD"?"orange":
"red"
}}>
{acc.status}
</td>

<td>

<button style={holdBtn}
onClick={()=>holdAccount(acc.accountNumber)}>
HOLD
</button>

<button style={unholdBtn}
onClick={()=>unholdAccount(acc.accountNumber)}>
UNHOLD
</button>

<button style={blockBtn}
onClick={()=>blockAccount(acc.accountNumber)}>
BLOCK
</button>

</td>

</tr>

))}

</tbody>

</table>

)}

</div>

);

}

const container={
background:"#f4f6f9",
padding:"20px",
minHeight:"100vh"
};

const formStyle={
background:"white",
padding:"50px",
width:"400px",
border:"block",
boxShadow:"0 0 10px #ccc"
};

const inputStyle={
width:"100%",
padding:"10px",
border:"block",
marginTop:"30px"
};

const errorStyle={
color:"red",
fontSize:"14px"
};

const createButton={
width:"100%",
padding:"10px",
background:"#27ae60",
color:"white",
border:"block",
marginTop:"10px"
};

const listButton={
padding:"10px",
background:"#2980b9",
color:"white",
border:"block",
marginTop:"20px"
};

const holdBtn={
background:"#f39c12",
color:"white",
border:"none",
padding:"5px",
marginRight:"5px"
};

const unholdBtn={
background:"#27ae60",
color:"white",
border:"none",
padding:"5px",
marginRight:"5px"
};

const blockBtn={
background:"#e74c3c",
color:"white",
border:"none",
padding:"5px"
};

const tableStyle={
width:"100%",
marginTop:"20px",
background:"white",
borderCollapse:"collapse"
};

const headerStyle={
background:"#34495e",
color:"white"
};

export default AccountPage;