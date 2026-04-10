import React,{useState} from "react";
import CustomerService from "../services/CustomerService";

function CustomerPage(){

const [firstName,setFirstName]=useState("");
const [lastName,setLastName]=useState("");
const [email,setEmail]=useState("");
const [mobile,setMobile]=useState("");

const [customers,setCustomers]=useState([]);
const [savedCustomer,setSavedCustomer]=useState(null);

const [showList,setShowList]=useState(false);
const [error,setError]=useState("");

const validate=()=>{

if(!firstName){
setError("First Name required");
return false;
}

if(!lastName){
setError("Last Name required");
return false;
}

if(!email){
setError("Email required");
return false;
}

if(!mobile){
setError("Mobile required");
return false;
}

setError("");
return true;

};


const createCustomer=(e)=>{

e.preventDefault();

if(!validate()) return;

const data={
firstName,
lastName,
email,
mobile
};

CustomerService.createCustomer(data)
.then(res=>{
setSavedCustomer(res.data);

setFirstName("");
setLastName("");
setEmail("");
setMobile("");

})
.catch(err=>{
if(err.response){
setError(err.response.data);
}else{
setError("Server error");
}
});

};


const toggleCustomers=()=>{

if(showList){
setShowList(false);
return;
}

CustomerService.getCustomers()
.then(res=>{
setCustomers(res.data);
setShowList(true);
});

};


const refreshCustomers=()=>{
CustomerService.getCustomers()
.then(res=>setCustomers(res.data));
};


const deleteCustomer=(id)=>{
CustomerService.deleteCustomer(id)
.then(()=>refreshCustomers());
};


return(

<div style={container}>

<h2>Customer Registration</h2>

<form style={formStyle} onSubmit={createCustomer}>

<input
placeholder="First Name"
value={firstName}
onChange={(e)=>setFirstName(e.target.value)}
style={inputStyle}
/>

<input
placeholder="Last Name"
value={lastName}
onChange={(e)=>setLastName(e.target.value)}
style={inputStyle}
/>

<input
placeholder="Email"
value={email}
onChange={(e)=>setEmail(e.target.value)}
style={inputStyle}
/>

<input
placeholder="Mobile"
value={mobile}
onChange={(e)=>setMobile(e.target.value)}
style={inputStyle}
/>

{error && <p style={errorStyle}>{error}</p>}

<button style={createButton}>
Create Customer
</button>

</form>

{/* Saved */}

{savedCustomer &&(

<div>

<h3>Saved Customer</h3>

<table style={tableStyle}>

<thead>
<tr style={headerStyle}>
<th>ID</th>
<th>First</th>
<th>Last</th>
<th>Email</th>
<th>Mobile</th>
</tr>
</thead>

<tbody>

<tr style={{textAlign:"center"}}>

<td>{savedCustomer.id}</td>
<td>{savedCustomer.firstName}</td>
<td>{savedCustomer.lastName}</td>
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
<th>First</th>
<th>Last</th>
<th>Email</th>
<th>Mobile</th>
<th>Action</th>
</tr>

</thead>

<tbody>

{customers.map(c=>(

<tr key={c.id} style={{textAlign:"center"}}>

<td>{c.id}</td>
<td>{c.firstName}</td>
<td>{c.lastName}</td>
<td>{c.email}</td>
<td>{c.mobile}</td>

<td>
<button
style={deleteBtn}
onClick={()=>deleteCustomer(c.id)}
>
DELETE
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
padding:"20px"
};

const formStyle={
background:"white",
padding:"20px",
width:"400px"
};

const inputStyle={
width:"100%",
padding:"10px",
marginTop:"10px"
};

const createButton={
width:"100%",
padding:"10px",
background:"#27ae60",
color:"white",
marginTop:"10px"
};

const listButton={
padding:"10px",
background:"#2980b9",
color:"white",
marginTop:"20px"
};

const deleteBtn={
background:"#e74c3c",
color:"white",
border:"none",
padding:"5px"
};

const tableStyle={
width:"100%",
marginTop:"20px",
background:"white"
};

const headerStyle={
background:"#34495e",
color:"white"
};

const errorStyle={
color:"red"
};

export default CustomerPage;