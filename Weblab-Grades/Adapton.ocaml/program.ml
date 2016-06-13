(* to run in interpreter the following 3 lines, to compile, remove these lines *)
#use "topfind";;
#require "adapton";;
#require "ppx_deriving.std";;

open Adapton;;

module AInt = MakeArt.Of (Name) (Types.Int);;
module AFloat = MakeArt.Of (Name) (Types.Float);;
module AFloatOption = MakeArt.Of(Name)(Types.Option (Types.Float));;

class submission =
  object (self)
    val manualGrade = AFloatOption.cell (Name.of_string "manualGrade") None
    method set_manualGrade a = AFloatOption.set manualGrade a
    method manualGrade = AFloatOption.force manualGrade
  end;;

let lab1Alice = new submission;;
let lab2Alice = new submission;;
lab1Alice#manualGrade;;
lab2Alice#manualGrade;;
lab1Alice#set_manualGrade (Some 10.0);;
lab2Alice#set_manualGrade (Some 6.0);;
lab1Alice#manualGrade;;
lab2Alice#manualGrade;;
lab2Alice#set_manualGrade (Some 7.0);;
lab1Alice#manualGrade;;
lab2Alice#manualGrade;;


let x = AFloat.cell (Name.of_string "x") 10.0;;
let y = AFloat.cell (Name.of_string "y") 20.0;;
AFloat.force x;;
AFloat.force y;;

let plus_five = (AFloat.mk_mfn (Name.of_string "plus_five") (module AFloat)
                   (fun memo -> fun x -> AFloat.force x +. 5.0)).AFloat.mfn_nart;;

let z = plus_five (Name.of_string "z") x;;
AFloat.force z;;
let a = plus_five (Name.of_string "a") z;;
AFloat.force a;;
AFloat.set x 3.0;;
AFloat.force a;;


module AFloatPlus =
struct
  type t = AFloat.t * AFloat.t
      [@@deriving eq, ord, show]
  let hash seed = function
    | (t, t') -> AFloat.hash (AFloat.hash seed t) t'
  let sanitize = function
    | (t, t') ->  (AFloat.sanitize t, AFloat.sanitize t')

end;;

let plus_float = (AFloat.mk_mfn (Name.of_string "plus_five") (module AFloatPlus)
                    (fun memo -> function | (x, y) -> AFloat.force x +. AFloat.force y)).AFloat.mfn_nart;;

let b = plus_float (Name.of_string "b") (x, y);;
AFloat.force b;;
AFloat.set y 4.0;;
AFloat.force b;;
let c = plus_five (Name.of_string "c") b;;
AFloat.force c;;
let d = plus_float (Name.of_string "d") (c, y);;
AFloat.force d;;



