let items;

function getOrders(){
document.getElementById("orders").innerHTML="";
$.ajax({
url:"/getOrders",
method:"POST",
success:function(response){
items=(JSON.parse(response))
let counter=1;
(JSON.parse(response)).forEach((item)=>{
let row=document.createElement("tr")
if(item.status=="Pending"){
row.innerHTML=`<td>${counter}</td><td>${item.id}</td><td>${item.items}</td><td>${item.status}</td>
<td><div class="dropdown">
<button class="btn btn-outline-warning drop-down-toggle" data-bs-toggle="dropdown">Action</button>
<div class="dropdown-menu">
<div class="dropdown-item" id="${item.id}" onclick="cancel(this.id)">
Cancel
</div>
<div class="dropdown-item" id="${item.id}" onclick="deliver(this.id)">
Mark Delivered
</div>
</div>
</div>
</td>
`;

}else{
row.innerHTML=`<td>${counter}</td><td>${item.id}</td><td>${item.items}</td><td>${item.status}</td>`;
}
document.getElementById("orders").appendChild(row)
counter++;
})
}
,error:function(message){
console.log(message)
}
})
}


function cancel(id){
$.ajax({
url:"/changeStatus",
method:"POST",
dataType:"json",
contentType:"application/json; charset=utf-8",
data:JSON.stringify({status:"Cancelled",orderId:id}),
success:function(response){
console.log(response)
getOrders()
},
error:function (mess){
window.location.reload()
}
}
)
}

function deliver(id){
$.ajax({
url:"/changeStatus",
method:"POST",
dataType:"json",
contentType:"application/json; charset=utf-8",
data:JSON.stringify({status:"Delivered",orderId:id}),
success:function(response){
window.location.reload()
}
,error:function(message){
window.location.reload()
console.log(message.responseText)
}
})
}

getOrders();