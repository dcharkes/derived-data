fn main() {
    let mut math_alice = Submission1 { children: vec![], answer: None, manual_grade: None };
    let mut exam_alice = Submission2 { children: vec![], answer: Some("Good".to_string()), manual_grade: Some(8.0) };
    let mut lab_alice = Submission2 { children: vec![], answer: None, manual_grade: None };
    let mut lab1_alice = Submission3 { answer: Some("Perfect".to_string()), manual_grade: Some(10.0) };
    let mut lab2_alice = Submission3 { answer: Some("Sufficient".to_string()), manual_grade: Some(6.0) };
    math_alice.children = vec![exam_alice, lab_alice];
    lab_alice.children = vec![lab1_alice, lab2_alice];
    println!("Hello, world!");
    //    println!("{:?}", grade(lab1_alice));
}

struct Submission1 {
    children: Vec<Submission2>,
    answer: Option<String>,
    manual_grade: Option<f32>
}

struct Submission2 {
    children: Vec<Submission3>,
    answer: Option<String>,
    manual_grade: Option<f32>
}

struct Submission3 {
    answer: Option<String>,
    manual_grade: Option<f32>
}

fn grade(s: &Submission3) -> Option<f32> {
    s.manual_grade
}