//
//  BackgroundDecorAnimated.swift
//  iosApp
//
//  Created by  Kseniia Feskova on 02.04.2026.
//

import SwiftUI

final class BackgroundViewModel: ObservableObject {
    @Published var circles: [MovingCircle] = []

    private var lastTime: TimeInterval = 0

    func setup(size: CGSize) {
        circles = makeCircles(size: size)
    }

    func update(time: TimeInterval, size: CGSize) {
        let delta = lastTime == 0 ? 0 : time - lastTime
        lastTime = time

        for i in circles.indices {
            circles[i].update(screen: size, delta: delta)
        }
    }
}

struct BackgroundDecorAnimated: View {

    @StateObject private var vm = BackgroundViewModel()

    var body: some View {
        GeometryReader { geo in

            TimelineView(.animation) { timeline in
                let time = timeline.date.timeIntervalSince1970
                let size = geo.size

                Canvas { context, _ in
                    for circle in vm.circles {
                        let rect = CGRect(
                            x: circle.x,
                            y: circle.y,
                            width: circle.size,
                            height: circle.size
                        )

                        context.fill(
                            Path(ellipseIn: rect),
                            with: .color(circle.color)
                        )
                    }
                }
                .onAppear {
                    vm.setup(size: size)
                }
                .onChange(of: time) { newTime in
                    vm.update(time: newTime, size: size)
                }
            }
        }.background(AppColor.background.edgesIgnoringSafeArea(.all))

    }
}


struct BackgroundDecorAnimated_Previews: PreviewProvider {
    static var previews: some View {
        BackgroundDecorAnimated()
    }
}
