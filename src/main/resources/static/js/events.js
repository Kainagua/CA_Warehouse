
setTimeout(()=>{
$.ajax({
method:"POST",
url:"http://localhost:8082/events",
success:function(response){
console.log(response)
response.forEach((event)=>{
let div=document.createElement("div")
div.classList.add("list-item")
div.innerText=event.message
document.getElementById("events").appendChild(div)
})
}
})
},2000)
