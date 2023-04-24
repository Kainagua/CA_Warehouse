function saveItem(){
let name=$("#itemName").val()
let quantity=$("#itemQuantity").val()
let price=$("#itemPrice").val()

if(!name || !quantity || !price){
alert("All fields are required")
return;
}
$.ajax({
url:"/addItem",
method:"POST",
dataType:"json",
contentType:"application/json; charset=utf-8",
data:JSON.stringify({
itemName:name,
quantity:quantity,
itemPrice:price
}),
success:function(response){

if(response=="success"){
getItems();
}
}
,error:function(message){
console.log(message)
getItems()
}
})
}

function getItems(){
document.getElementById("items").innerHTML="";
$.ajax({
url:"/getItems",
method:"POST",
success:function(response){
let counter=1;
(JSON.parse(response)).forEach((item)=>{
console.log(item)
let row=document.createElement("tr")
row.innerHTML=`<td>${counter}</td><td>${item.itemName}</td><td>${item.quantity}</td><td>${item.itemPrice}</td>
<td><div class="dropdown">
<button class="btn btn-outline-warning drop-down-toggle" data-bs-toggle="dropdown">Action</button>
<div class="dropdown-menu">
<div class="dropdown-item" id="${item.id}" onclick="deleteItem(this.id);">
Delete
</div>
</div>
</div>
</td>
`;
document.getElementById("items").appendChild(row)
counter++;
})
}
,error:function(message){
console.log(message)
}
})
}

getItems();

function deleteItem(id){
$.ajax({
url:"/deleteItem",
method:"POST",
dataType:"json",
contentType:"application/json; charset=utf-8",
data:JSON.stringify({
itemId:id
}),
success:function(response){

},error:function(message){
window.location.reload()
}
})

}