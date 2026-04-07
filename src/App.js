import React from "react";
import {BrowserRouter as Router,Routes,Route,Link} from "react-router-dom";

import CustomerPage from "./components/CustomerPage";
import AccountPage from "./components/AccountPage";
import TransactionPage from "./components/TransactionPage";

function App(){

return(

<Router>

<div style={{padding:"20px"}}>

<h1>BANK-LOAN-APPLICATION</h1>

<nav>

<Link to="/customers" style={{marginRight:"30px"}}>
Customer Service
</Link>

<Link to="/accounts" style={{marginRight:"30px"}}>
Account Service
</Link>

<Link to="/transactions">
Transaction Service
</Link>

</nav>

<hr/>

<Routes>

<Route path="/customers" element={<CustomerPage/>}/>

<Route path="/accounts" element={<AccountPage/>}/>

<Route path="/transactions" element={<TransactionPage/>}/>

</Routes>

</div>

</Router>

);

}

export default App;