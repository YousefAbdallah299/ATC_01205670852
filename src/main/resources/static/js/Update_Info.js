const student = JSON.parse(localStorage.getItem("regestired"));


function find(array, email) {
    for (var i = 0; i < array.length; i++) {
        if (array[i].email == email) {
            return array[i];


        }
    }

    return false;
}

document.getElementById("Fname").value = student.Fname;
document.getElementById("Lname").value = student.Lname;
document.getElementById("id").value = student.id;
document.getElementById("date").value = student.date;
document.getElementById("gpa").value = student.gpa;
document.getElementById("gender").value = student.gender;
document.getElementById("level").value = student.level;
document.getElementById("status").value = student.status;
document.getElementById("email").value = student.email;
document.getElementById("phone").value = student.phone;
document.getElementById("dep").value = student.department;

function check_level() {
    var level = document.getElementById("level").value;
    if (level < 3) {
        document.getElementById("dep").value = "General";
        document.getElementById("dep").disabled = true;

    } else {
        document.getElementById("dep").value = student.department;
        document.getElementById("dep").disabled = false;

    }
}
window.onload = check_level;

function update() {
    let id = document.getElementById("id").value;
    let gpa = document.getElementById("gpa").value;
    let email = document.getElementById("email").value;
    let level = document.getElementById("level").value;
    let status = document.getElementById("status").value;
    let dep = document.getElementById("dep").value;


    const all = JSON.parse(localStorage.getItem("data"));
    const pattern = /\S+@\S+\.\S+/;
    var check = find(all, email)
    if (check && id != check.id ) {
        alert("this email belongs to another studnet")
    } else {
        if (email != "" && gpa != "" && level != "" && status != "" && dep != "" &&
            (gpa < 4 & gpa > 0) && pattern.test(email)) {
            
            for (var i = 0; i < all.length; i++) {

                if (all[i].id == student.id) {
                    all[i].gpa = document.getElementById("gpa").value;
                    all[i].level = document.getElementById("level").value;
                    all[i].status = document.getElementById("status").value;
                    all[i].email = document.getElementById("email").value;
                    all[i].phone = document.getElementById("phone").value;
                    all[i].department = document.getElementById("dep").value;
                    window.localStorage.setItem("regestired", JSON.stringify(all[i]));


                }
            }
            window.localStorage.setItem("data", JSON.stringify(all));

            alert("Student has been updated");
            window.location.href = "Home_Page.html";
        }
    }
}
function rst() {
    document.getElementById("Fname").value = student.Fname;
    document.getElementById("Fname").value = student.Lname;
    document.getElementById("id").value = student.id;
    document.getElementById("date").value = student.date;
    document.getElementById("gpa").value = null;
    document.getElementById("level").value = null;
    document.getElementById("gender").value = student.gender;
    document.getElementById("status").value = null;
    document.getElementById("email").value = null;
    document.getElementById("phone").value = null;
    document.getElementById("dep").value = student.department;
}


   

function remove() {
    var result = confirm("Want to delete?");
    if (result) {
        const all = JSON.parse(localStorage.getItem("data"));
        for (var i = 0; i < all.length; i++) {
            if (all[i].id == student.id) {
                all.splice(i, 1);
            }
        }
        window.localStorage.setItem("data", JSON.stringify(all));

        alert("Student has been deleted");
        window.location.href = "Home_Page.html";
    }
    
}