use std::env;

use dlopen2::wrapper::{Container, WrapperApi};

#[derive(WrapperApi)]
struct RunnableSo {
  run: fn() -> (),
}

fn main() {
  let dll = env::var("LOAD_DLL").expect("Unable to find LOAD_DLL environment variable");

  let run: Container<RunnableSo> =
    unsafe { Container::load(dll) }.expect("Unable to load dynalic library");

  // We give this process to the so file so that it can execute as it likes
  run.run();
}
