(* to run in interpreter the following 3 lines, to compile, remove these lines *)
#use "topfind";;
#require "adapton";;
#require "ppx_deriving.std";;

open Adapton;;

module ABool = MakeArt.Of (Name) (Types.Bool);;
module AFloat = MakeArt.Of (Name) (Types.Float);;
module AFloatOption = MakeArt.Of(Name)(Types.Option (Types.Float));;

let gradeLambda = 
  (AFloatOption.mk_mfn 
     (Name.of_string "gradeLambda") 
     (module Types.Tuple3 (AFloatOption) (AFloatOption) (ABool))
     (fun memo -> function | (manualGrade, childGrade, childPass) ->
      match AFloatOption.force manualGrade with
          (Some g) -> (Some g)
        | None -> if ABool.force childPass then AFloatOption.force childGrade else None)
  ).AFloatOption.mfn_nart;;

let passLambda =
  (ABool.mk_mfn 
     (Name.of_string "gradePass") 
     (module Types.Tuple2 (AFloatOption)  (ABool))
     (fun memo -> function | (grade, childPass) ->
        let gradePass = match AFloatOption.force grade with
            (Some g) -> g >= 5.5
          | None    -> false
        in
          gradePass && ABool.force childPass)
  ).ABool.mfn_nart;;

class submission =
  fun () ->
    let object_name = Name.gensym () in
    let field_name a = (Name.pair object_name (Name.of_string a)) in
    let manualGrade = AFloatOption.cell (field_name "manualGrade") None
    and childGrade = AFloatOption.cell (field_name "childGrade") None
    and childPass = ABool.cell (field_name "childPass") true in
    let grade = gradeLambda (field_name "grade") (manualGrade, childGrade, childPass) in
    let pass = passLambda (field_name "pass") (grade, childPass) in
      object
        val childGrade = childGrade
        val childPass = childPass
        val manualGrade = manualGrade
        val grade = grade
        val pass = pass
        method set_manualGrade a = AFloatOption.set manualGrade a
        method manualGrade = AFloatOption.force manualGrade
        method set_childGrade a = AFloatOption.set childGrade a
        method childGrade = AFloatOption.force childGrade
        method set_childPass  a = ABool.set childPass a
        method childPass = ABool.force childPass
        method grade = AFloatOption.force grade
        method pass = ABool.force pass
      end;;

let lab1Alice = new submission ();;
let lab2Alice = new submission ();;
lab1Alice#grade;;
lab2Alice#grade;;
lab1Alice#set_manualGrade (Some 10.0);;
lab2Alice#set_childGrade (Some 6.0);;
lab1Alice#grade;;
lab2Alice#grade;;
lab2Alice#set_manualGrade (Some 7.0);;
lab1Alice#grade;;
lab2Alice#grade;;


