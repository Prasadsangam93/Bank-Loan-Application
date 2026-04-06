import React,{useState} from "react";
import CustomerService from "../services/CustomerService";

function CustomerPage(){

const [customers,setCustomers] = useState([]);
const [showList,setShowList] = useState(false);
const [savedCustomer,setSavedCustomer] = useState(null);

const [id,setId] = useState(null);

const [firstName,setFirstName] = useState("");
const [lastName,setLastName] = useState("");
const [email,setEmail] = useState("");
const [mobile,setMobile] = useState("");

const [errors,setErrors] = useState({});

const validate = () =>{

let error={};

if(!firstName){
error.firstName="First Name Required";
}
else if(firstName.length < 3){
error.firstName="First Name minimum 3 characters";
}
else if(firstName.length > 30){
error.firstName="First Name maximum 30 characters";
}

if(!lastName){
error.lastName="Last Name Required";
}
else if(lastName.length < 3){
error.lastName="Last Name minimum 3 characters";
}
else if(lastName.length > 30){
error.lastName="Last Name maximum 30 characters";
}

if(!email){
error.email="Email Required";
}
else if(!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(email)){
error.email="Invalid Email";
}

if(!mobile){
error.mobile="Mobile Required";
}
else if(!/^[0-9]{10}$/.test(mobile)){
error.mobile="Mobile must be 10 digits";
}

setErrors(error);

return Object.keys(error).length===0;

};

const saveCustomer=(e)=>{

e.preventDefault();

if(!validate()) return;

const data={
firstName,
lastName,
email,
mobile
};

if(id){

CustomerService.updateCustomer(id,data)
.then(res=>{
setSavedCustomer(res.data);
clearForm();
fetchCustomers();
});

}
else{

CustomerService.createCustomer(data)
.then(res=>{
setSavedCustomer(res.data);
clearForm();
fetchCustomers();
});

}

};

const fetchCustomers=()=>{

CustomerService.getCustomers()
.then(res=>{
setCustomers(res.data);
setShowList(true);
});

};

const toggleCustomers=()=>{

if(showList){
setShowList(false);
}else{
fetchCustomers();
}

};

const editCustomer=(c)=>{

setId(c.id);
setFirstName(c.firstName);
setLastName(c.lastName);
setEmail(c.email);
setMobile(c.mobile);

};

const deleteCustomer=(cid)=>{

CustomerService.deleteCustomer(cid)
.then(()=>{
fetchCustomers();
});

};

const clearForm=()=>{

setId(null);
setFirstName("");
setLastName("");
setEmail("");
setMobile("");
setErrors({});

};

return(

<div style={container}>

<h2>Customer Register</h2>

<form style={formStyle} onSubmit={saveCustomer}>

<input
placeholder="First Name"
value={firstName}
onChange={(e)=>setFirstName(e.target.value)}
style={inputStyle}
/>
<p style={errorStyle}>{errors.firstName}</p>

<input
placeholder="Last Name"
value={lastName}
onChange={(e)=>setLastName(e.target.value)}
style={inputStyle}
/>
<p style={errorStyle}>{errors.lastName}</p>

<input
placeholder="Email"
value={email}
onChange={(e)=>setEmail(e.target.value)}
style={inputStyle}
/>
<p style={errorStyle}>{errors.email}</p>

<input
placeholder="Mobile"
value={mobile}
onChange={(e)=>setMobile(e.target.value)}
style={inputStyle}
/>
<p style={errorStyle}>{errors.mobile}</p>

<button style={saveButton}>
{id ? "Update Customer" : "Save Customer"}
</button>

</form>

{/* Saved Customer */}

{savedCustomer &&(

<div>

<h3>Saved Customer</h3>

<table style={tableStyle}>

<thead>
<tr style={headerStyle}>
<th>ID</th>
<th>Name</th>
<th>Email</th>
<th>Mobile</th>
</tr>
</thead>

<tbody>

<tr style={{textAlign:"center"}}>
<td>{savedCustomer.id}</td>
<td>{savedCustomer.firstName} {savedCustomer.lastName}</td>
<td>{savedCustomer.email}</td>
<td>{savedCustomer.mobile}</td>
</tr>

</tbody>

</table>

</div>

)}

<hr/>

<button style={listButton} onClick={toggleCustomers}>
{showList ? "Hide Customers" : "Show Customers"}
</button>

{showList &&(

<table style={tableStyle}>

<thead>

<tr style={headerStyle}>
<th>ID</th>
<th>Name</th>
<th>Email</th>
<th>Mobile</th>
<th>Status</th>
<th>KYC</th>
<th>Action</th>
</tr>

</thead>

<tbody>

{customers.map(c=>(

<tr key={c.id} style={{textAlign:"center"}}>

<td>{c.id}</td>
<td>{c.firstName} {c.lastName}</td>
<td>{c.email}</td>
<td>{c.mobile}</td>
<td>{c.status}</td>
<td>{c.kycStatus}</td>

<td>

<button
type="button"
style={updateButton}
onClick={()=>editCustomer(c)}
>
Update
</button>

<button
type="button"
style={deleteButton}
onClick={()=>deleteCustomer(c.id)}
>
Delete
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
background:"#f9f4f4cb",
padding:"50px",
minHeight:"100vh"
};

const formStyle={
background:"white",
padding:"50px",
width:"400px",

boxShadow:"0 0 10px #cccccc"
};

const inputStyle={
width:"100%",
padding:"10px",
marginTop:"20px"
};

const errorStyle={
color:"red",
fontSize:"12px"
};

const saveButton={
width:"50%",
padding:"10px",
background:"#27ae91",
color:"white",
border:"block",
marginTop:"10px"
};

const listButton={
padding:"10px",
background:"#29b941",
color:"white",
border:"block",
marginTop:"20px"
};

const updateButton={
background:"#f39c12",
color:"white",
border:"none",
padding:"5px",
marginRight:"5px"
};

const deleteButton={
background:"#e74c3c",
color:"white",
border:"none",
padding:"5px"
};

const tableStyle={
width:"100%",
marginTop:"20px",
borderCollapse:"collapse",
background:"white"
};

const headerStyle={
background:"#34495e",
color:"white"
};

export default CustomerPage;