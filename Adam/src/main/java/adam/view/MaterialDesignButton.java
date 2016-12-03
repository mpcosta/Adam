package adam.view;

import com.sun.javafx.scene.control.skin.ButtonSkin;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class MaterialDesignButton extends Button {

	private Circle circleRipple;
	private Rectangle rippleClip = new Rectangle();
	private Duration rippleDuration = Duration.millis(250);
	private double lastRippleHeight = 0;
	private double lastRippleWidth = 0;
	private Color rippleColor = new Color(0, 0, 0, 0.11);

	public MaterialDesignButton(String text, Color rippleColor) {
		super(text);
		setStyle(rippleColor);
	}

	private void setStyle(Color color) {
		getStyleClass().addAll("material-design-button");
		createRippleEffect();
		circleRipple.setFill(color);
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		final ButtonSkin buttonSkin = new ButtonSkin(this);
		this.getChildren().add(0, circleRipple);

		return buttonSkin;
	}

	private void createRippleEffect() {
		circleRipple = new Circle(0.1, rippleColor);
		circleRipple.setOpacity(0.0);

		final FadeTransition fadeTransition = new FadeTransition(rippleDuration, circleRipple);
		fadeTransition.setInterpolator(Interpolator.EASE_OUT);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);

		final Timeline scaleRippleTimeline = new Timeline();

		final SequentialTransition parallelTransition = new SequentialTransition();
		parallelTransition.getChildren().addAll(scaleRippleTimeline, fadeTransition);

		parallelTransition.setOnFinished(event1 -> {
			circleRipple.setOpacity(0.0);
			circleRipple.setRadius(0.1);
		});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			parallelTransition.stop();
			parallelTransition.getOnFinished().handle(null);

			circleRipple.setCenterX(event.getX());
			circleRipple.setCenterY(event.getY());

			if (getWidth() != lastRippleWidth || getHeight() != lastRippleHeight) {
				lastRippleWidth = getWidth();
				lastRippleHeight = getHeight();

				rippleClip.setWidth(lastRippleWidth);
				rippleClip.setHeight(lastRippleHeight);

				try {
					rippleClip.setArcHeight(
							this.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
					rippleClip.setArcWidth(
							this.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
					circleRipple.setClip(rippleClip);
				} catch (Exception e) {
					e.printStackTrace();
				}

				double circleRippleRadius = Math.max(getHeight(), getWidth()) * 0.65;
				final KeyValue keyValue = new KeyValue(circleRipple.radiusProperty(), circleRippleRadius,
						Interpolator.EASE_OUT);
				final KeyFrame keyFrame = new KeyFrame(rippleDuration, keyValue);
				scaleRippleTimeline.getKeyFrames().clear();
				scaleRippleTimeline.getKeyFrames().add(keyFrame);
			}

			parallelTransition.playFromStart();
		});
	}
}
