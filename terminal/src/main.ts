import { Terminal } from '@xterm/xterm';
import { FitAddon } from "@xterm/addon-fit"
import { ClipboardAddon } from '@xterm/addon-clipboard';
import { ImageAddon } from '@xterm/addon-image';
import { WebLinksAddon } from '@xterm/addon-web-links';

import { path, pathToVisible, shell } from './shell';

const terminal = new Terminal();

const fit = new FitAddon();

terminal.loadAddon(new ClipboardAddon())
terminal.loadAddon(new ImageAddon())
terminal.loadAddon(new WebLinksAddon())
terminal.loadAddon(fit)
terminal.open(document.getElementById('terminal')!);

fit.fit()

// let lineBuffer: any[] = [];
// let history = [];
// let shellListener: any = null;

// async function simpleShell(data: any) {
//   // string splitting is needed to also handle multichar input (eg. from copy)
//   for (let i = 0; i < data.length; ++i) {
//     const c = data[i];
//     if (c === '\r') {  // <Enter> was pressed case
//       terminal.write('\r\n');
//       if (lineBuffer.length) {
//         // we have something in line buffer, normally a shell does its REPL logic here
//         // for simplicity - just join characters and exec...
//         const command = lineBuffer.join('');
//         lineBuffer.length = 0;
//         history.push(command);
//         try {
//           // tricky part: for interactive sub commands you have to detach the shell listener
//           // temporarily, and re-attach after the command was finished
//           shellListener?.dispose();
//           //await exec(command);  // issue: cannot force-kill in JS (needs to be a good citizen)
//         } catch (e) {
//           // we have no real process separation with STDERR
//           // simply catch any error and output in red
//           const msg = 'Unknown Error...';
//           terminal.write(`\x1b[31m${msg.replace('\n', '\r\n')}\x1b[m`);
//         } finally {
//           // in any case re-attach shell
//           shellListener = terminal.onData(simpleShell);
//         }
//         terminal.write('\r\n');
//       }
//       terminal.write('SimpleShell> ');
//     } else if (c === '\x7F') {  // <Backspace> was pressed case
//       if (lineBuffer.length) {
//         // dont delete prompt
//         // this is still wrong for multiline inputs!
//         lineBuffer.pop();
//         terminal.write('\b \b');
//       }
//     } else if (['\x1b[A', '\x1b[B', '\x1b[C', '\x1b[D'].includes(data.slice(i, i + 3))) {  // <arrow> keys pressed
//       if (data.slice(i, i + 3) === '\x1b[A') {
//         // UP pressed, select backwards from history + erase terminal line + write history entry
//         terminal.write('\x1b[2KSimpleShell> ' + history_entry);
//       } else if (data.slice(i, i + 3) === '\x1b[B') {
//         // UP pressed, select forward from history + erase terminal line + write history entry
//         terminal.write('\x1b[2KSimpleShell> ' + history_entry);
//       }
//       // <LEFT> <RIGHT> skipped, since no inline editing implemented
//       i += 2;
//     } else {  // push everything else into the line buffer and echo back to user
//       lineBuffer.push(c);
//       terminal.write(c);
//     }
//   }
// }

// shell startup
shell(terminal)

terminal.write(`\x1b[94mminshell\x1b[0m\n\rThis is a minimal shell-like interface.\r
\x1b[1mCommands:\x1b[0m\r
  dir - Get Dir Name\r
  ls - List directory content\r
  cd - Change directory to absolute path only\r
  cdui - Change directory (interactive)\r
  leadman - Runs leadman binary\r
  lead - Runs lead binary\r
  clear - Clears the terminal\r\n
minshell@${pathToVisible(path)} : `);