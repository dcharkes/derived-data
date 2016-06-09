let conjunction = List.fold_left ( && ) true;;
let sum = List.fold_left ( +. ) 0.0;;
let avg a = match a with
    [] -> None
  | _  -> Some (sum a /. float_of_int (List.length a));;
let flatten a = List.flatten (List.map (fun x->match x with Some(y) -> [y] | None -> []) a);;

class submission =
  object (self)
    val mutable children : submission list = [] 
    val mutable answer = ""
    val mutable manualGrade : float option = None
    method set_children a = children <- a ; self
    method set_answer a = answer <- a ; self
    method answer = answer
    method set_manualGrade a = manualGrade <- a ; self
    method manualGrade = manualGrade
    method childGrade : float option =
      let grades = flatten (List.map (fun x->x#grade) children) 
      in
        avg grades
    method childPass : bool =
      conjunction (List.map (fun x->x#pass) children) 
    method grade : float option =
      match self#manualGrade with
          (Some g) -> (Some g)
        | None -> if self#childPass then self#childGrade else None
    method pass : bool =
      let gradePass = match self#grade with
          (Some g) -> g >= 5.5
        | None    -> false
      in
        gradePass && self#childPass
  end;;

let mathAlice = new submission;;
let examAlice = new submission;;
let labAlice = new submission;;
let lab1Alice = new submission;;
let lab2Alice = new submission;;
mathAlice#set_children [examAlice; labAlice];;
labAlice#set_children [lab1Alice; lab2Alice];;
examAlice#set_answer "Good";;
examAlice#set_manualGrade (Some 8.0);;
lab1Alice#set_answer "Perfect";;
lab1Alice#set_manualGrade (Some 10.0);;
lab2Alice#set_answer "Sufficient";;
lab2Alice#set_manualGrade (Some 6.0);;
let mathBob = new submission;;
let examBob = new submission;;
let labBob = new submission;;
let lab1Bob = new submission;;
let lab2Bob = new submission;;
mathBob#set_children [examBob; labBob];;
labBob#set_children [lab1Bob; lab2Bob];;
examBob#set_answer "Very Good";;
examBob#set_manualGrade (Some 9.0);;
lab1Bob#set_answer "Insufficient";;
lab1Bob#set_manualGrade (Some 3.0);;
lab2Bob#set_answer "Perfect";;
lab2Bob#set_manualGrade (Some 10.0);;

"Alice";;
mathAlice#grade;;
mathAlice#pass;;
"Bob";;
mathBob#grade;;
mathBob#pass;;

