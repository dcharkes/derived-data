use std::rc::Rc;
use std::cell::RefCell;

fn main() {
    let math_alice = Submission::new(None, None);
    let exam_alice = Submission::new(Some("Good".to_string()), Some(8.0));
    let lab_alice = Submission::new(None, None);
    let lab1_alice = Submission::new(Some("Perfect".to_string()), Some(10.0));
    let lab2_alice = Submission::new(Some("Sufficient".to_string()), Some(6.0));
    {
        let mut mut_math_alice = math_alice.borrow_mut();
        mut_math_alice.children.push(exam_alice.clone());
        mut_math_alice.children.push(lab_alice.clone());
        let mut mut_lab_alice = lab_alice.borrow_mut();
        mut_lab_alice.children.push(lab1_alice.clone());
        mut_lab_alice.children.push(lab2_alice.clone());
    }
    let math_bob = Submission::new(None, None);
    let exam_bob = Submission::new(Some("Very Good".to_string()), Some(9.0));
    let lab_bob = Submission::new(None, None);
    let lab1_bob = Submission::new(Some("Insufficient".to_string()), Some(3.0));
    let lab2_bob = Submission::new(Some("Perfect".to_string()), Some(10.0));
    {
        let mut mut_math_bob = math_bob.borrow_mut();
        mut_math_bob.children.push(exam_bob.clone());
        mut_math_bob.children.push(lab_bob.clone());
        let mut mut_lab_bob = lab_bob.borrow_mut();
        mut_lab_bob.children.push(lab1_bob.clone());
        mut_lab_bob.children.push(lab2_bob.clone());
    }
    println!("Alice");
    let math_alice = math_alice.borrow();
    println!("{:?}", math_alice.grade());
    println!("{:?}", math_alice.pass());
    println!("");
    println!("Bob");
    let math_bob = math_bob.borrow();
    println!("{:?}", math_bob.grade());
    println!("{:?}", math_bob.pass());
}

#[allow(dead_code)]
struct Submission {
    children: Vec<Rc<RefCell<Submission>>>,
    answer: Option<String>,
    manual_grade: Option<f32>
}
impl Submission {
    fn new(answer: Option<String>, manual_grade: Option<f32>) -> Rc<RefCell<Submission>> {
        Rc::new(RefCell::new(Submission {
            children: Vec::new(),
            answer: answer,
            manual_grade: manual_grade,
        }))
    }

    fn child_grade(&self) -> Option<f32> {
        let grades: Vec<f32> = self.children.iter().flat_map(|x| -> Option<f32>{
            x.borrow().grade()
        }).collect();
        avg(&grades)
    }

    fn child_pass(&self) -> bool {
        let passes: Vec<bool> = self.children.iter().map(|x| -> bool{
            x.borrow().pass()
        }).collect();
        conj(&passes)
    }

    fn grade(&self) -> Option<f32> {
        match self.manual_grade {
            Some(x) => Some(x),
            None => {
                if self.child_pass() {
                    self.child_grade()
                } else {
                    None
                }
            }
        }
    }

    fn pass(&self) -> bool {
        let g = self.grade();
        match g {
            Option::None => false,
            Some(v) => v >= 5.5
        }
    }
}

fn sum(numbers: &Vec<f32>) -> f32 {
    numbers.iter().fold(0.0, |p, &q| p + q)
}
fn avg(numbers: &Vec<f32>) -> Option<f32> {
    if numbers.is_empty() {
        None
    } else {
        Some(sum(numbers) / numbers.len() as f32)
    }
}
fn conj(bools: &Vec<bool>) -> bool {
    bools.iter().fold(true, |p, &q| p && q)
}

/*
 * Plain Rust: not incremental
 */